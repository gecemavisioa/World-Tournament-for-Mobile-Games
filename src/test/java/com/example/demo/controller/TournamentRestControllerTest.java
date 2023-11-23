package com.example.demo.controller;

import com.example.demo.dto.CountryLeaderboard.CountryLeaderboard;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TournamentRestControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentRestController tournamentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void EnterTournament_WhenTournamentIsActive_ShouldReturnOk() {
        int userId = 1;
        when(tournamentService.isActive()).thenReturn(true);
        when(tournamentService.enterTournament(userId)).thenReturn(new GroupLeaderboard());

        ResponseEntity<GroupLeaderboard> response = tournamentController.enterTournament(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tournamentService, times(1)).enterTournament(userId);
    }

    @Test
    void EnterTournament_WhenTournamentIsNotActive_ThrowsForbiddenException() {
        int userId = 1;
        when(tournamentService.isActive()).thenReturn(false);

        ForbiddenException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ForbiddenException.class,
                () -> tournamentController.enterTournament(userId)
        );

        assertEquals("There is no tournament going on!", exception.getMessage());
        verify(tournamentService, never()).enterTournament(userId);
    }

    @Test
    void GetCountryLeaderboard_ShouldReturnCountryLeaderboard() {
        int page = 1;
        int size = 10;
        when(tournamentService.getCountryLeaderboard(page, size)).thenReturn(new CountryLeaderboard());

        ResponseEntity<CountryLeaderboard> response = tournamentController.getCountryLeaderboard(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tournamentService, times(1)).getCountryLeaderboard(page, size);
    }
}
