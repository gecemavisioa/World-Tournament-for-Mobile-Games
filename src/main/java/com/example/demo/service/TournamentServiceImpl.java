package com.example.demo.service;

import com.example.demo.dto.CountryLeaderboard.CountryLeaderboard;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.entity.*;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.QueueException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.util.CountryHelper;
import com.example.demo.util.ProcessTournamentResults;
import com.example.demo.util.UserQueue;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TournamentServiceImpl implements TournamentService {
    private TournamentRepository tournamentRepository;
    private TournamentGroupRepository tournamentGroupRepository;
    private GroupUserRepository groupUserRepository;
    private UserRepository userRepository;
    private ProcessTournamentResults processTournamentResults;
    private RewardRepository rewardRepository;
    private final TaskExecutor taskExecutor;
    private final Lock lock = new ReentrantLock();
    private final List<Integer> processPaymentFailed = new ArrayList<>();
    private UserQueue queue;
    private Tournament currentTournament;
    private boolean isActive;

    @Autowired
    public TournamentServiceImpl(RewardRepository rewardRepository, GroupUserRepository groupUserRepository, UserRepository userRepository, TournamentRepository tournamentRepository, TaskExecutor taskExecutor, ProcessTournamentResults processTournamentResults, TournamentGroupRepository tournamentGroupRepository) {
        this.tournamentRepository = tournamentRepository;
        this.taskExecutor = taskExecutor;
        this.processTournamentResults = processTournamentResults;
        this.userRepository = userRepository;
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.groupUserRepository = groupUserRepository;
        this.rewardRepository = rewardRepository;
    }

    @PostConstruct
    public void restoreTournamentFromDB() {
        currentTournament = tournamentRepository.findByDate(ZonedDateTime.now().getYear(), ZonedDateTime.now().getMonthValue(), ZonedDateTime.now().getDayOfMonth());
        if (currentTournament == null) {
            currentTournament = tournamentRepository.save(new Tournament(ZonedDateTime.now()));
        }
        if(ZonedDateTime.now().getHour() >= 20) {
            endTournament();
        }
        else{
            isActive = true;
            queue = new UserQueue();
        }

    }

    @Scheduled(cron = "00 00 00 * * *")
    public void startTournament() {
        isActive = true;
        queue = new UserQueue();
        currentTournament = tournamentRepository.save(new Tournament(ZonedDateTime.now()));
    }

    @Scheduled(cron = "00 00 20 * * *")
    public void endTournament() {
        isActive = false;
        List<TournamentGroup> tournamentGroups = tournamentRepository.findAllGroupsByTournamentId(currentTournament.getId()).getTournamentGroups();

        // Split tournament groups according to the thread pool size
        // And process them asynchronously at the same time
        // We want users get their rewards as soon as possible
        // with the multithreaded nature of @Async annotation
        // process function ensures this
        int groupCount = tournamentGroups.size();
        int poolSize = ((ThreadPoolTaskExecutor) taskExecutor).getCorePoolSize();
        int threadLoad = (groupCount / poolSize) + 1;

        for(int i = 0; i < groupCount; i += threadLoad) {
            int toIdx = i + threadLoad;
            if(toIdx > groupCount) {
                toIdx = groupCount;
            }
            processTournamentResults.process(tournamentGroups.subList(i, toIdx), currentTournament.getDate());
        }
    }

    @Override
    public boolean isActive() {
        return ZonedDateTime.now().getHour() < 20;
    }

    @Override
    public CountryLeaderboard getCountryLeaderboard(int page, int size) {
        List<Long> scores = new ArrayList<>();
        List<List<GroupUser>> users = new ArrayList<>();
        Pageable pages;
        if(page == 0 && size == 0) pages = Pageable.unpaged();
        else pages = PageRequest.of(page, size);

        for(String country: CountryHelper.getCountryList()) {
            scores.add(groupUserRepository.findTotalScoreByTournamentIdAndCountry(currentTournament.getId(), country));
            users.add(groupUserRepository.findAllUsersByTournamentId(currentTournament.getId(), country, pages));
        }

        return new CountryLeaderboard(scores, users);
    }

    @Override
    public GroupLeaderboard enterTournament(int userId) {
        User theUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User could not be founded"));

        if (theUser.getLevel() < 20) throw new ForbiddenException("U need to be level 20!");
        if (theUser.getCoins() < 1000) throw new ForbiddenException("U need to have 1000 coins!");

        if (rewardRepository.findByUserId(userId) != null)
            throw new ForbiddenException("Before entering a new tournament, you need to get your reward!");

        int countryIdx = CountryHelper.getCountryIndex(theUser.getCountry());

        // This area shares a common lock with removing people from UserQueue's inQueue set
        // Its purpose is explained below in createGroup function
        synchronized (lock) {
            // Checking if user is already in tournament and returning leaderboard
            GroupUser inTournament = groupUserRepository.findByUserIdAndTournamentId(userId, currentTournament.getId())
                    .orElse(null);
            ;
            if (inTournament != null) {
                List<GroupUser> groupUsers = tournamentGroupRepository.findAllGroupUsersById(inTournament.getGroup().getId()).getGroupUsers();
                return new GroupLeaderboard(groupUsers);
            }

            // If not in tournament then check if inQueue
            if (queue.getInQueue().contains(userId)) throw new QueueException("You are already in a queue!");

            // All conditions are met, add user to inQueue set
            queue.addToInQueue(userId);
        }

        // Send user to UserQueue and if a group can be formed return group
        // Otherwise addQueue function throws waiting for other players error
        List<Integer> group = queue.addQueue(userId, countryIdx);
        return createGroup(group);

    }

    @Transactional
    public GroupLeaderboard createGroup(List<Integer> group) {
        // Second check if tournament is active before saving group to db
        if(!isActive) throw new ForbiddenException("There is no tournament going on!");

        // Save the group to database
        TournamentGroup tempGroup = new TournamentGroup();

        List<GroupUser> tempGroupUsers = new ArrayList<>();

        for (Integer u : group) {
            GroupUser tempGroupUser = new GroupUser();
            User tempUser = userRepository.findById(u).get();
            tempGroupUser.setUser(tempUser);
            tempGroupUsers.add(tempGroupUser);
        }

        tempGroup.setTournament(currentTournament);
        tempGroup.setGroupUsers(tempGroupUsers);
        tournamentGroupRepository.save(tempGroup).getGroupUsers();
        List<GroupUser> dbGroupUsers = tournamentGroupRepository.findAllGroupUsersById(tempGroup.getId()).getGroupUsers();

        // Since this payment can produce Optimistic Locking Failure Exception
        // The exception can be written in a log file and payment can be processed later on
        // This would a more robust solution
        // i.e: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect) : [com.example.demo.entity.User#12]
        // As another approach, I created a list that holds user ids of unprocessed payments
        // And a scheduled function that checks unprocessed payments every minute to process them.
        for(Integer u: group) {
            try {
                userRepository.save(userRepository.findById(u).get().payTournamentFee());
            }
            catch (OptimisticLockingFailureException ex) {
                synchronized (processPaymentFailed) {
                    processPaymentFailed.add(u);
                }
            }
        }

        // Remove people from UserQueue.InQueue set
        // This lock is shared the area above
        // Its purpose is preventing the following situation:
        // when a group is formed but still not saved to database
        // same person can enter the queue
        synchronized (lock){
            for(int u : group) {
                queue.removeFromInQueue(u);
            }
        }
        return new GroupLeaderboard(dbGroupUsers);
    }

    @Override
    public GroupLeaderboard getGroupLeaderboard(int userId) {
        GroupUser tempGroupUser = groupUserRepository.findByUserIdAndTournamentId(userId, currentTournament.getId())
                .orElse(null);
        if(tempGroupUser == null) throw new UserNotFoundException("User haven't joined the tournament!");
        List<GroupUser> groupUsers = tournamentGroupRepository.findAllGroupUsersById(tempGroupUser.getGroup().getId()).getGroupUsers();

        return new GroupLeaderboard(groupUsers);
    }

    @Override
    @Transactional
    public void incrementUserTournamentScore(int userId) {
        GroupUser tempGroupUser = groupUserRepository.findByUserIdAndTournamentId(userId, currentTournament.getId())
                .orElse(null);
        if(tempGroupUser != null) {
            tempGroupUser.incrementScore();
            groupUserRepository.save(tempGroupUser);
        }
    }

    // Checking process payment fails list and process again every minute
    // If process success remove user from list
    // Otherwise log and continue
    @Transactional
    @Scheduled(cron = "00 * * * * *")
    public void processPaymentAgain() {
        synchronized (processPaymentFailed){
            for(Integer u: processPaymentFailed) {
                try {
                    userRepository.save(userRepository.findById(u).get().payTournamentFee());
                    processPaymentFailed.remove(u);
                }
                catch (OptimisticLockingFailureException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    // Setters for unit-testing
    public void setCurrentTournament(Tournament currentTournament) {
        this.currentTournament = currentTournament;
    }

    public void setQueue(UserQueue queue) {
        this.queue = queue;
    }

    public Tournament getCurrentTournament() {
        return currentTournament;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}