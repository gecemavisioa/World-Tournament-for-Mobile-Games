package com.example.demo.util;

import com.example.demo.entity.GroupUser;

import java.util.ArrayList;
import java.util.List;

public class SameScores {

    // This function is used to determine users with same score
    // If 2 user shares the same score their rank is the highest one
    // Next user stays at her/his original rank
    // Returns a list with corresponding ranks
    public static List<Integer> sameOnesRanker(List<GroupUser> groupUsers) {
        List<Integer> ranks = new ArrayList<>(5);
        for(int i = 0; i < 5; i++) {
            if(i == 0) {
                ranks.add(1);
            }
            else {
                if(groupUsers.get(i).getScore() == groupUsers.get(i - 1).getScore()) {
                    ranks.add(ranks.get(i - 1));
                }
                else {
                    ranks.add(i + 1);
                }
            }
        }

        return ranks;
    }
}
