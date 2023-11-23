package com.example.demo.dto.GroupLeaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupLeaderboardUsers {
    private int userId;
    private String username;
    private String country;
    private int score;
}
