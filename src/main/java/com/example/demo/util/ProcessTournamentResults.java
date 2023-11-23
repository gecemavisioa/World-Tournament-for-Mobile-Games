package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ProcessTournamentResults {

    // This component process tournament results every day at 20
    // process function is called from tournament service
    // In order to prevent bypassing @Async notation and growing large in tournament service
    // This util class implemented

    private TournamentGroupRepository tournamentGroupRepository;
    private GroupUserRepository groupUserRepository;
    private UserRankRepository userRankRepository;
    private RewardRepository rewardRepository;

    @Autowired
    public ProcessTournamentResults(RewardRepository rewardRepository, UserRankRepository userRankRepository, TournamentGroupRepository tournamentGroupRepository, GroupUserRepository groupUserService) {
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.groupUserRepository = groupUserService;
        this.userRankRepository = userRankRepository;
        this.rewardRepository = rewardRepository;
    }

    @Async
    @Transactional
    public void process(List<TournamentGroup> tournamentGroups, ZonedDateTime date) {
        for(TournamentGroup tg: tournamentGroups) {
            List<GroupUser> groupUsers = tournamentGroupRepository.findAllGroupUsersById(tg.getId()).getGroupUsers();

            // Checking if there are same scores and share prize
            List<Integer> sameScores = SameScores.sameOnesRanker(groupUsers);
            int firstPrizeCount = (int) sameScores.stream().filter(x -> x == 1).count();
            int secondPrizeCount = (int) sameScores.stream().filter(x -> x == 2).count();

            for(int i = 0; i < 5; i++) {
                GroupUser tempGroupUser = groupUsers.get(i);
                // If an interruption occurs while processing
                // isProcessed field prevents this function process it 2nd time
                if(tempGroupUser.isProcessed()) continue;

                User tempUser = tempGroupUser.getUser();
                int currentRank = sameScores.get(i);


                if(currentRank == 1) {
                    rewardRepository.save(new Reward(tempUser.getId(), 10000 / firstPrizeCount));
                }
                if(currentRank == 2) {
                    rewardRepository.save(new Reward(tempUser.getId(), 5000 / secondPrizeCount));
                }

                UserRank tempRank = new UserRank(date, currentRank);
                tempRank.setUser(tempUser);
                userRankRepository.save(tempRank);

                tempGroupUser.setProcessed(true);

                groupUserRepository.save(tempGroupUser);
            }
        }
    }
}
