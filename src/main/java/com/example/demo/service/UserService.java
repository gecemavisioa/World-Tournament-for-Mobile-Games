package com.example.demo.service;

import com.example.demo.dto.GroupRank.GroupRanks;
import com.example.demo.entity.User;

public interface UserService {
    User createUser(String username);
    User findUserById(int id);
    User proceedLevel(int id);
    User receiveReward(int id);
    GroupRanks findPastRanksByUserId(int userId);

}
