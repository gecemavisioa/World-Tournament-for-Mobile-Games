package com.example.demo.dto.GroupRank;

import com.example.demo.entity.UserRank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupRanks {
    private List<GroupRank> ranks;

    public GroupRanks(List<UserRank> userRanks) {
        this.ranks = generateRanks(userRanks);
    }

    public List<GroupRank> generateRanks(List<UserRank> userRanks) {
        List<GroupRank> groupRank = new ArrayList<>();
        for(UserRank ur: userRanks) {
            groupRank.add(new GroupRank(ur.getTournamentDate(), ur.getRank()));
        }
        return groupRank;
    }
}
