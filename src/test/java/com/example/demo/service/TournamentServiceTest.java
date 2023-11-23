package com.example.demo.service;

import com.example.demo.dto.CountryLeaderboard.CountryLeaderboard;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.entity.*;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.repository.*;
import com.example.demo.util.CountryHelper;
import com.example.demo.util.UserQueue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private TournamentGroupRepository tournamentGroupRepository;
    @Mock
    private GroupUserRepository groupUserRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private ZonedDateTime date;
    private Tournament tournament;
    private TournamentGroup tournamentGroup;
    private User u1;
    private User u2;
    private User u3;
    private User u4;
    private User u5;
    private GroupUser gu1;
    private GroupUser gu2;
    private GroupUser gu3;
    private GroupUser gu4;
    private GroupUser gu5;
    @BeforeEach
    public void init() {
        date = ZonedDateTime.now();
        tournament = Tournament.builder().id(1).date(date).build();

        tournamentGroup = TournamentGroup.builder().id(1).build();

        u1 = User.builder().id(1).username("Elliott").level(20).coins(2000).country("Turkey").build();
        gu1 = GroupUser.builder().user(u1).id(1).isProcessed(false).build();
        u2 = User.builder().id(2).username("Elliott").level(20).coins(2000).country("the United States").build();
        gu2 = GroupUser.builder().user(u2).id(2).isProcessed(false).build();
        u3 = User.builder().id(3).username("Elliott").level(20).coins(2000).country("the United Kingdom").build();
        gu3 = GroupUser.builder().user(u3).id(3).isProcessed(false).build();
        u4 = User.builder().id(4).username("Elliott").level(20).coins(2000).country("France").build();
        gu4 = GroupUser.builder().user(u4).id(4).isProcessed(false).build();
        u5 = User.builder().id(5).username("Elliott").level(20).coins(2000).country("Germany").build();
        gu5 = GroupUser.builder().user(u5).id(5).isProcessed(false).build();
    }

    @Test
    public void TournamentService_StartTournament() {
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        tournamentService.startTournament();

        Assertions.assertThat(tournamentService.getCurrentTournament()).isEqualTo(tournament);
    }


    @Test
    public void TournamentService_IsActive_ReturnsBoolean() {
        boolean result = tournamentService.isActive();

        Assertions.assertThat(result).isEqualTo(new Date().getHours() < 20);
    }

    @Test
    public void TournamentService_GetAllTournamentUsers_ReturnsCountryLeaderboard() {
        tournamentService.setCurrentTournament(tournament);
        gu1.setScore(4);
        gu2.setScore(3);
        gu3.setScore(2);
        gu4.setScore(1);
        gu5.setScore(0);
        tournamentGroup.setTournament(tournament);
        gu1.setGroup(tournamentGroup);
        gu2.setGroup(tournamentGroup);
        gu3.setGroup(tournamentGroup);
        gu4.setGroup(tournamentGroup);
        gu5.setGroup(tournamentGroup);
        List<GroupUser> list1 = new ArrayList<>(Arrays.asList(gu1));
        List<GroupUser> list2 = new ArrayList<>(Arrays.asList(gu1));
        List<GroupUser> list3 = new ArrayList<>(Arrays.asList(gu1));
        List<GroupUser> list4 = new ArrayList<>(Arrays.asList(gu1));
        List<GroupUser> list5 = new ArrayList<>(Arrays.asList(gu1));
        when(groupUserRepository.findTotalScoreByTournamentIdAndCountry(1, "Turkey")).thenReturn((long) gu1.getScore());
        when(groupUserRepository.findAllUsersByTournamentId(1, "Turkey", PageRequest.of(0, 1))).thenReturn(list1);
        when(groupUserRepository.findTotalScoreByTournamentIdAndCountry(1, "the United States")).thenReturn((long) gu2.getScore());
        when(groupUserRepository.findAllUsersByTournamentId(1, "the United States", PageRequest.of(0, 1))).thenReturn(list2);
        when(groupUserRepository.findTotalScoreByTournamentIdAndCountry(1, "the United Kingdom")).thenReturn((long) gu3.getScore());
        when(groupUserRepository.findAllUsersByTournamentId(1, "the United Kingdom", PageRequest.of(0, 1))).thenReturn(list3);
        when(groupUserRepository.findTotalScoreByTournamentIdAndCountry(1, "France")).thenReturn((long) gu4.getScore());
        when(groupUserRepository.findAllUsersByTournamentId(1, "France", PageRequest.of(0, 1))).thenReturn(list4);
        when(groupUserRepository.findTotalScoreByTournamentIdAndCountry(1, "Germany")).thenReturn((long) gu5.getScore());
        when(groupUserRepository.findAllUsersByTournamentId(1, "Germany", PageRequest.of(0, 1))).thenReturn(list5);

        CountryLeaderboard result = tournamentService.getCountryLeaderboard(0, 1);
        CountryLeaderboard expected = new CountryLeaderboard(new ArrayList<>(Arrays.asList((long) gu1.getScore(), (long) gu2.getScore(), (long) gu3.getScore(), (long) gu4.getScore(), (long) gu5.getScore())), new ArrayList<>(Arrays.asList(list1, list2, list3, list4, list5)));

        Assertions.assertThat(result.toString()).isEqualTo(expected.toString());
    }

    @Test
    public void TournamentService_EnterTournament_ReturnsGroupLeaderboard() {
        tournamentService.setCurrentTournament(tournament);
        tournamentGroup.setTournament(tournament);
        tournamentGroup.setGroupUsers(new ArrayList<>(Arrays.asList(gu1, gu2, gu3, gu4, gu5)));
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(u1));
        when(userRepository.findById(2)).thenReturn(Optional.ofNullable(u2));
        when(userRepository.findById(3)).thenReturn(Optional.ofNullable(u3));
        when(userRepository.findById(4)).thenReturn(Optional.ofNullable(u4));
        when(userRepository.findById(5)).thenReturn(Optional.ofNullable(u5));
        when(tournamentGroupRepository.findAllGroupUsersById(any(Integer.class))).thenReturn(tournamentGroup);
        when(tournamentGroupRepository.save(any(TournamentGroup.class))).thenReturn(tournamentGroup);
        when(userRepository.save(any(User.class))).thenReturn(u1);
        UserQueue userQueue = new UserQueue();
        userQueue.getQueues().get(CountryHelper.getCountryIndex(u1.getCountry())).add(1);
        userQueue.getQueues().get(CountryHelper.getCountryIndex(u2.getCountry())).add(2);
        userQueue.getQueues().get(CountryHelper.getCountryIndex(u3.getCountry())).add(3);
        userQueue.getQueues().get(CountryHelper.getCountryIndex(u4.getCountry())).add(4);
        userQueue.addToInQueue(1);
        userQueue.addToInQueue(2);
        userQueue.addToInQueue(3);
        userQueue.addToInQueue(4);
        tournamentService.setQueue(userQueue);
        tournamentService.setActive(true);
        GroupLeaderboard expected = new GroupLeaderboard(List.of(gu1, gu2, gu3, gu4, gu5));
        GroupLeaderboard created = tournamentService.enterTournament(5);

        Assertions.assertThat(created.toString()).isEqualTo(expected.toString());
        Assertions.assertThat(u1.getCoins()).isEqualTo(1000);

    }

    @Test
    public void TournamentService_EnterTournament_ThrowsForbiddenException() {
        u1.setCoins(0);
        u2.setLevel(5);
        when(userRepository.findById(1)).thenReturn(Optional.of(u1));
        when(userRepository.findById(2)).thenReturn(Optional.of(u2));
        when(userRepository.findById(3)).thenReturn(Optional.of(u3));
        when(rewardRepository.findByUserId(3)).thenReturn(new Reward(3, 10000));
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            tournamentService.enterTournament(1);
        });
        ForbiddenException exception2 = assertThrows(ForbiddenException.class, () -> {
            tournamentService.enterTournament(2);
        });
        ForbiddenException exception3 = assertThrows(ForbiddenException.class, () -> {
            tournamentService.enterTournament(3);
        });

        assertEquals("U need to have 1000 coins!", exception.getMessage());
        assertEquals("U need to be level 20!", exception2.getMessage());
        assertEquals("Before entering a new tournament, you need to get your reward!", exception3.getMessage());
    }

    @Test
    public void TournamentService_GetGroupLeaderboard_ReturnsGroupLeaderboard() {
        tournamentService.setCurrentTournament(tournament);
        tournamentGroup.setGroupUsers(List.of(gu1, gu2, gu3, gu4, gu5));
        tournamentGroup.setTournament(tournament);
        when(groupUserRepository.findByUserIdAndTournamentId(1, 1)).thenReturn(Optional.ofNullable(gu1));
        when(tournamentGroupRepository.findAllGroupUsersById(1)).thenReturn(tournamentGroup);

        GroupLeaderboard foundLeaderboard = tournamentService.getGroupLeaderboard(1);
        GroupLeaderboard expected = new GroupLeaderboard(tournamentGroup.getGroupUsers());

        Assertions.assertThat(foundLeaderboard.toString()).isEqualTo(expected.toString());
    }

    @Test
    public void TournamentService_IncrementUserTournamentScore() {
        tournamentService.setCurrentTournament(tournament);
        tournamentGroup.setGroupUsers(List.of(gu1, gu2, gu3, gu4, gu5));
        tournamentGroup.setTournament(tournament);
        when(groupUserRepository.findByUserIdAndTournamentId(1, 1)).thenReturn(Optional.ofNullable(gu1));
        when(groupUserRepository.save(any(GroupUser.class))).thenReturn(gu1);

        tournamentService.incrementUserTournamentScore(gu1.getUser().getId());

        Assertions.assertThat(gu1.getScore()).isEqualTo(1);
    }
}
