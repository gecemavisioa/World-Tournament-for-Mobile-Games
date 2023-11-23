package com.example.demo.service;

import com.example.demo.dto.GroupRank.GroupRanks;
import com.example.demo.entity.Reward;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRank;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.repository.RewardRepository;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_CreateUser_ReturnsUser() {
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser("Elliott");

        Assertions.assertThat(savedUser)
                .isNotNull();
        Assertions.assertThat(savedUser.getUsername())
                .isEqualTo(user.getUsername());
        Assertions.assertThat(savedUser.getId())
                .isEqualTo(user.getId());
    }

    @Test
    public void UserService_FindUserById_ReturnUser() {
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.findUserById(1);

        Assertions.assertThat(foundUser)
                .isNotNull();
        Assertions.assertThat(foundUser.getUsername())
                .isEqualTo(user.getUsername());
        Assertions.assertThat(foundUser.getId())
                .isEqualTo(1);
    }

    @Test
    public void UserService_ProceedLevel_ReturnsUser(){
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .level(1)
                .coins(0)
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User proceededUser = userService.proceedLevel(1);

        Assertions.assertThat(proceededUser)
                .isNotNull();
        Assertions.assertThat(proceededUser.getLevel())
                .isEqualTo(2);
        Assertions.assertThat(proceededUser.getCoins())
                .isEqualTo(25);
    }

    @Test
    public void UserService_ReceiveReward_ReturnsUser(){
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .level(1)
                .coins(0)
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(rewardRepository.findByUserId(1)).thenReturn(new Reward(1, 10000));
        User proceededUser = userService.receiveReward(1);

        Assertions.assertThat(proceededUser)
                .isNotNull();
        Assertions.assertThat(proceededUser.getCoins())
                .isEqualTo(10000);
    }

    @Test
    public void UserService_ReceiveReward_ThrowsForbiddenException(){
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .level(1)
                .coins(0)
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(rewardRepository.findByUserId(1)).thenReturn(null);
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            userService.receiveReward(1);
        });

        assertEquals("You don't have any rewards!", exception.getMessage());
    }

    @Test
    public void UserService_FindPastRanksByUserId_ReturnsListOfUserRanks(){
        UserRank userRank = new UserRank(ZonedDateTime.now(), 1);
        User user = User.builder()
                .id(1)
                .username("Elliott")
                .pastRanks(List.of(userRank))
                .build();

        when(userRepository.findAllRanksByUserId(1)).thenReturn(Optional.ofNullable(user));

        GroupRanks foundPastRanks = userService.findPastRanksByUserId(1);

        Assertions.assertThat(foundPastRanks)
                .isNotNull();
        Assertions.assertThat(foundPastRanks.toString())
                .isEqualTo(foundPastRanks.toString());
    }

}
