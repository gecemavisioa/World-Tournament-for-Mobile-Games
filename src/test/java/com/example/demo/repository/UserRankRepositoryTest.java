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

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRankRepositoryTest {

    @Autowired
    private UserRankRepository userRankRepository;

    @Test
    public void UserRankRepository_Save_ReturnUserRank() {
        User user = User.builder().build();

        UserRank userRank = UserRank.builder()
                .tournamentDate(ZonedDateTime.now())
                .user(user)
                .build();

        UserRank savedUserRank = userRankRepository.save(userRank);

        Assertions.assertThat(savedUserRank)
                .isNotNull();
        Assertions.assertThat(savedUserRank.getTournamentDate())
                .isEqualTo(userRank.getTournamentDate());
        Assertions.assertThat(savedUserRank.getUser())
                .isEqualTo(user);
    }
}
