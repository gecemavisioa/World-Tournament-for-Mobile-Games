package com.example.demo.dto.CountryLeaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryLeaderboardUsers {
    private int userId;
    private String username;
    private int score;
}
