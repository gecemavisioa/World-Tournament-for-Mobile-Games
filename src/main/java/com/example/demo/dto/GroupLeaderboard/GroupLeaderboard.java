package com.example.demo.dto.GroupLeaderboard;

import com.example.demo.entity.GroupUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupLeaderboard {

    private List<GroupLeaderboardUsers> Leaderboard;

    public GroupLeaderboard(List<GroupUser> groupUsers) {
        Leaderboard = generateLeaderboard(groupUsers);
    }

    public List<GroupLeaderboardUsers> generateLeaderboard(List<GroupUser> groupUsers) {
        List<GroupLeaderboardUsers> tempLeaderboard = new ArrayList<>();
        for(GroupUser gu: groupUsers) {
            tempLeaderboard.add(new GroupLeaderboardUsers(gu.getUser().getId(), gu.getUser().getUsername(), gu.getUser().getCountry(), gu.getScore()));
        }

        return tempLeaderboard;
    }
}
