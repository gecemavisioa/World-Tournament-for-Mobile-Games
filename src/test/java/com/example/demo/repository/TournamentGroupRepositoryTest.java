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
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TournamentGroupRepositoryTest {

    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserRepository userRepository;

    private Tournament tournament;
    private User user;
    private User user2;

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
    }

    @Test
    public void TournamentGroupRepository_Save_ReturnTournamentGroup() {
        TournamentGroup tournamentGroup = TournamentGroup.builder()
                .tournament(tournament)
                .build();

        TournamentGroup foundTournamentGroup = tournamentGroupRepository.save(tournamentGroup);

        Assertions.assertThat(foundTournamentGroup)
                .isNotNull();
        Assertions.assertThat(foundTournamentGroup.getId())
                .isGreaterThan(0);
    }

    @Test
    public void TournamentGroupRepository_FindAllGroupUsersById_ReturnTournamentGroup() {
        TournamentGroup tournamentGroup = TournamentGroup.builder()
                .tournament(tournament)
                .build();

        List<GroupUser> groupUsers = new ArrayList<>();
        GroupUser groupUser = GroupUser.builder()
                .group(tournamentGroup)
                .user(user)
                .build();
        GroupUser groupUser1 = GroupUser.builder()
                .group(tournamentGroup)
                .user(user2)
                .build();
        groupUsers.add(groupUser);
        groupUsers.add(groupUser1);

        tournamentGroup.setGroupUsers(groupUsers);
        tournamentGroupRepository.save(tournamentGroup);

        TournamentGroup foundTournamentGroup = tournamentGroupRepository.findAllGroupUsersById(tournamentGroup.getId());

        Assertions.assertThat(foundTournamentGroup)
                .isNotNull();
        Assertions.assertThat(foundTournamentGroup.getGroupUsers().size())
                .isEqualTo(2);
    }
}
