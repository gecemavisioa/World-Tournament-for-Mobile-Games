package com.example.demo.service;

import com.example.demo.dto.GroupRank.GroupRanks;
import com.example.demo.entity.Reward;
import com.example.demo.entity.User;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private RewardRepository rewardRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository, RewardRepository rewardRepository) {
        userRepository = theUserRepository;
        this.rewardRepository = rewardRepository;
    }

    @Override
    @Transactional
    public User createUser(String username) {
        User temp = new User(username);
        temp.setId(0);
        return userRepository.save(temp);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User could not be founded"));
    }

    @Override
    @Transactional
    public User proceedLevel(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User could not be founded"));

        return userRepository.save(user.proceedLevel());
    }

    @Override
    @Transactional
    public User receiveReward(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User could not be founded"));

        Reward reward = rewardRepository.findByUserId(id);
        if(reward == null) throw new ForbiddenException("You don't have any rewards!");

        user.receiveReward(reward.getAmount());
        rewardRepository.delete(reward);
        return userRepository.save(user);
    }

    @Override
    public GroupRanks findPastRanksByUserId(int userId) {
        User eagerLoad = userRepository.findAllRanksByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User could not be founded"));
        return new GroupRanks(eagerLoad.getPastRanks());
    }

}
