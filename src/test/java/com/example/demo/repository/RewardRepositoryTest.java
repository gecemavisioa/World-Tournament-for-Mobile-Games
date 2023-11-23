package com.example.demo.repository;

import com.example.demo.entity.Reward;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RewardRepositoryTest {

    @Autowired
    private RewardRepository rewardRepository;

    private Reward reward;
    @BeforeEach
    public void init() {
        reward = Reward.builder()
                .id(1)
                .userId(1)
                .amount(10000)
                .build();
    }

    @Test
    public void RewardRepository_Save_ReturnReward() {
        Reward savedReward = rewardRepository.save(reward);

        Assertions.assertThat(savedReward)
                .isNotNull();
        Assertions.assertThat(savedReward.getId())
                .isGreaterThan(0);
        Assertions.assertThat(savedReward.getAmount())
                .isEqualTo(10000);
    }

    @Test
    public void RewardRepository_FindByUserId_ReturnReward() {
        rewardRepository.save(reward);

        Reward foundReward = rewardRepository.findByUserId(1);

        Assertions.assertThat(foundReward)
                .isNotNull();
    }

    @Test
    public void RewardRepository_Delete() {
        rewardRepository.save(reward);

        rewardRepository.delete(reward);
        Reward foundReward = rewardRepository.findByUserId(1);

        Assertions.assertThat(foundReward)
                .isNull();
    }
}