package com.example.demo.util;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessTournamentResultsTest {

    @Mock
    private TournamentGroupRepository tournamentGroupRepository;
    @Mock
    private UserRankRepository userRankRepository;
    @Mock
    private RewardRepository rewardRepository;
    @Mock
    private GroupUserRepository groupUserRepository;

    @InjectMocks
    private ProcessTournamentResults processTournamentResults;

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
        tournamentGroup.setTournament(tournament);

        u1 = User.builder().id(1).username("Elliott").level(20).coins(2000).country("Turkey").build();
        gu1 = GroupUser.builder().user(u1).id(1).score(30).isProcessed(false).build();
        u2 = User.builder().id(2).username("Elliott").level(20).coins(2000).country("the United States").build();
        gu2 = GroupUser.builder().user(u2).id(2).score(25).isProcessed(false).build();
        u3 = User.builder().id(3).username("Elliott").level(20).coins(2000).country("the United Kingdom").build();
        gu3 = GroupUser.builder().user(u3).id(3).score(25).isProcessed(false).build();
        u4 = User.builder().id(4).username("Elliott").level(20).coins(2000).country("France").build();
        gu4 = GroupUser.builder().user(u4).id(4).score(20).isProcessed(false).build();
        u5 = User.builder().id(5).username("Elliott").level(20).coins(2000).country("Germany").build();
        gu5 = GroupUser.builder().user(u5).id(5).score(10).isProcessed(false).build();

        tournamentGroup.setGroupUsers(new ArrayList<>(Arrays.asList(gu1, gu2, gu3, gu4, gu5)));
    }
    @Test
    public void ProcessTournamentResults_Process() {
        when(tournamentGroupRepository.findAllGroupUsersById(any(Integer.class))).thenReturn(tournamentGroup);
        when(groupUserRepository.save(any(GroupUser.class))).thenReturn(gu1);

        processTournamentResults.process(new ArrayList<>(Arrays.asList(tournamentGroup)), date);

        verify(rewardRepository, times(3)).save(any());
        verify(userRankRepository, times(5)).save(any());
        verify(groupUserRepository, times(5)).save(any());
    }
}