package com.example.demo.service;

import com.example.demo.dto.CountryLeaderboard.CountryLeaderboard;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;

public interface TournamentService {
    void startTournament();
    void endTournament();
    boolean isActive();
    CountryLeaderboard getCountryLeaderboard(int page, int size);
    GroupLeaderboard enterTournament(int userId);
    GroupLeaderboard getGroupLeaderboard(int userId);
    void incrementUserTournamentScore(int userId);

}
