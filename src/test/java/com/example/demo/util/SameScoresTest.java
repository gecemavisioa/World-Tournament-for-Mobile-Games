package com.example.demo.util;

import com.example.demo.entity.GroupUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SameScoresTest {

    private List<GroupUser> groupUsers;

    @BeforeEach
    public void init() {
        groupUsers = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            groupUsers.add(new GroupUser());
        }
    }

    @Test
    public void SameOnesRankerTest() {
        List<Integer> scores = List.of(100, 90, 90, 80, 70);
        for(int i = 0; i < 5; i++) {
            groupUsers.get(i).setScore(scores.get(i));
        }

        List<Integer> ranks = SameScores.sameOnesRanker(groupUsers);
        assertEquals(Arrays.asList(1, 2, 2, 4, 5), ranks);
    }

    @Test
    public void SameOnesRankerTest_AllDifferent() {
        List<Integer> scores = List.of(100, 90, 85, 80, 70);
        for(int i = 0; i < 5; i++) {
            groupUsers.get(i).setScore(scores.get(i));
        }

        List<Integer> ranks = SameScores.sameOnesRanker(groupUsers);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), ranks);
    }

    @Test
    public void SameOnesRankerTest_AllSame() {
        List<Integer> scores = List.of(100, 100, 100, 100, 100);
        for(int i = 0; i < 5; i++) {
            groupUsers.get(i).setScore(scores.get(i));
        }

        List<Integer> ranks = SameScores.sameOnesRanker(groupUsers);
        assertEquals(Arrays.asList(1, 1, 1, 1, 1), ranks);
    }

}
