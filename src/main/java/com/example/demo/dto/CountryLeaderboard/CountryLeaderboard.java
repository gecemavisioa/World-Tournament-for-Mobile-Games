package com.example.demo.dto.CountryLeaderboard;

import com.example.demo.entity.GroupUser;
import com.example.demo.util.CountryHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class CountryLeaderboard {

    private List<CountryLeaderboardCountry> Leaderboard;

    public CountryLeaderboard(List<Long> scores, List<List<GroupUser>> users) {
        Leaderboard = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Leaderboard.add(new CountryLeaderboardCountry(CountryHelper.getCountry(i), scores.get(i), users.get(i)));
        }
        Collections.sort(Leaderboard, Comparator.comparing(CountryLeaderboardCountry::getScore).reversed());
    }
}
