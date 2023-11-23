package com.example.demo.repository;

import com.example.demo.entity.GroupUser;
import com.example.demo.entity.Tournament;
import com.example.demo.entity.TournamentGroup;
import com.example.demo.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GroupUserRepositoryTest {

    @Autowired
    private GroupUserRepository groupUserRepository;
    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserRepository userRepository;

    private Tournament tournament;
    private User user;
    private User user2;
    private TournamentGroup tournamentGroup;

    @BeforeEach
    public void init() {
        tournament = tournamentRepository.save(
                Tournament.builder()
                        .id(1)
                        .date(ZonedDateTime.now())
                        .build()
        );

        user = userRepository.save(
                User.builder()
                        .id(1)
                        .username("Elliott")
                        .build()
        );
        user2 = userRepository.save(
                User.builder()
                        .id(2)
                        .username("Elliott")
                        .build()
        );

        tournamentGroup = tournamentGroupRepository.save(
                TournamentGroup.builder()
                .tournament(tournament)
                .build()
        );

    }

    @Test
    public void GroupUserRepository_Save_ReturnGroupUser() {
        GroupUser groupUser = GroupUser.builder()
                .user(user)
                .group(tournamentGroup)
                .build();

        GroupUser savedGroupUser = groupUserRepository.save(groupUser);

        Assertions.assertThat(savedGroupUser)
                .isNotNull();
        Assertions.assertThat(savedGroupUser.getUser())
                .isEqualTo(user);
        Assertions.assertThat(savedGroupUser.getGroup())
                .isEqualTo(tournamentGroup);
    }

    @Test
    public void GroupUserRepository_FindByUserIdAndTournamentId_ReturnsOptionalGroupUser() {
        GroupUser groupUser = GroupUser.builder()
                .user(user)
                .group(tournamentGroup)
                .build();

        groupUserRepository.save(groupUser);
        GroupUser foundGroupUser = groupUserRepository.findByUserIdAndTournamentId(user.getId(), tournament.getId()).get();

        Assertions.assertThat(foundGroupUser)
                .isNotNull();
        Assertions.assertThat(foundGroupUser.getUser())
                .isEqualTo(user);
        Assertions.assertThat(foundGroupUser.getGroup())
                .isEqualTo(tournamentGroup);
    }
}
