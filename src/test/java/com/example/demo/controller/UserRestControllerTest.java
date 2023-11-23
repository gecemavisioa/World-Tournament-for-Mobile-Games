package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.dto.GroupRank.GroupRanks;
import com.example.demo.service.TournamentService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRestControllerTest {

    @Mock
    private TournamentService tournamentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CreateUser_ShouldReturnCreatedUser() {
        User user = new User();
        user.setUsername("testUser");
        when(userService.createUser(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(user.getUsername());
    }

    @Test
    void ProceedLevel_WhenTournamentIsActive_ShouldIncrementTournamentScore() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userService.proceedLevel(userId)).thenReturn(user);
        when(tournamentService.isActive()).thenReturn(true);

        ResponseEntity<User> response = userController.proceedLevel(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).proceedLevel(userId);
        verify(tournamentService, times(1)).incrementUserTournamentScore(userId);
    }

    @Test
    void ProceedLevel_WhenTournamentIsNotActive_ShouldNotIncrementTournamentScore() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userService.proceedLevel(userId)).thenReturn(user);
        when(tournamentService.isActive()).thenReturn(false);

        ResponseEntity<User> response = userController.proceedLevel(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).proceedLevel(userId);
        verify(tournamentService, never()).incrementUserTournamentScore(userId);
    }

    @Test
    void GetGroupLeaderboard_ShouldReturnGroupLeaderboard() {
        int userId = 1;
        GroupLeaderboard groupLeaderboard = new GroupLeaderboard();
        when(tournamentService.getGroupLeaderboard(userId)).thenReturn(groupLeaderboard);

        ResponseEntity<GroupLeaderboard> response = userController.getGroupLeaderboard(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupLeaderboard, response.getBody());
        verify(tournamentService, times(1)).getGroupLeaderboard(userId);
    }

    @Test
    void ReceiveReward_ShouldReturnUserWithReceivedReward() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userService.receiveReward(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.receiveReward(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).receiveReward(userId);
    }

    @Test
    void GetPastRanks_ShouldReturnGroupRanks() {
        int userId = 1;
        GroupRanks groupRanks = new GroupRanks();
        when(userService.findPastRanksByUserId(userId)).thenReturn(groupRanks);

        ResponseEntity<GroupRanks> response = userController.getPastRanks(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupRanks, response.getBody());
        verify(userService, times(1)).findPastRanksByUserId(userId);
    }

}
