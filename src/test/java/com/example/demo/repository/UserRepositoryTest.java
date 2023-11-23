package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserRank;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnUser() {
        User user = User.builder()
                .username("Elliott")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser)
                .isNotNull();
        Assertions.assertThat(savedUser.getId())
                .isGreaterThan(0);
        Assertions.assertThat(savedUser.getUsername())
                .isEqualTo("Elliott");
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        User user = User.builder()
                .username("Elliott")
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).get();

        Assertions.assertThat(foundUser)
                .isNotNull();
    }

    @Test
    public void UserRepository_GetPastRanksByUserId_ReturnUser() {
        User user = User.builder()
                .username("Elliott")
                .build();

        UserRank userRank = UserRank.builder()
                .tournamentDate(ZonedDateTime.now())
                .rank(1)
                .build();
        UserRank userRank2 = UserRank.builder()
                .tournamentDate(ZonedDateTime.now())
                .rank(3)
                .build();

        user.setPastRanks(new ArrayList<>(Arrays.asList(userRank2, userRank)));

        userRepository.save(user);

        User foundUser = userRepository.findAllRanksByUserId(user.getId()).get();

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getPastRanks().size())
                .isEqualTo(2);
    }
}
