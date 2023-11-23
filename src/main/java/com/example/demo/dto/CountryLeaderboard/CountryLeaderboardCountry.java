package com.example.demo.dto.CountryLeaderboard;

import com.example.demo.entity.GroupUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CountryLeaderboardCountry {

    private String country;
    private Long score;
    private List<CountryLeaderboardUsers> users;

    public CountryLeaderboardCountry(String country, Long score, List<GroupUser> users) {
        this.country = country;
        this.score = score;
        if(score == null) this.score = (long) 0;
        this.users = new ArrayList<>();
        for(GroupUser u: users) {
            this.users.add(new CountryLeaderboardUsers(u.getUser().getId(), u.getUser().getUsername(), u.getScore()));
        }
    }

}
