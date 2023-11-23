package com.example.demo.repository;

import com.example.demo.entity.Tournament;
import com.example.demo.entity.TournamentGroup;
import org.assertj.core.api.Assertions;
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
public class TournamentRepositoryTest {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Test
    public void TournamentRepository_Save_ReturnTournament() {
        Tournament tournament = Tournament.builder()
                .date(ZonedDateTime.now())
                .build();

        Tournament savedTournament = tournamentRepository.save(tournament);

        Assertions.assertThat(savedTournament)
                .isNotNull();
        Assertions.assertThat(savedTournament.getId())
                .isGreaterThan(0);
        Assertions.assertThat(savedTournament.getDate())
                .isEqualTo(tournament.getDate());
    }

    @Test
    public void TournamentRepository_FindByDate_ReturnTournament() {
        ZonedDateTime date = ZonedDateTime.now();
        Tournament tournament = Tournament.builder()
                .date(date)
                .build();

        tournamentRepository.save(tournament);

        Tournament foundTournament = tournamentRepository.findByDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        Assertions.assertThat(foundTournament)
                .isNotNull();
        Assertions.assertThat(foundTournament.getDate())
                .isEqualTo(date);

    }

    @Test
    public void TournamentRepository_FindAllGroupsByTournamentId_ReturnTournament() {
        ZonedDateTime date = ZonedDateTime.now();
        Tournament tournament = Tournament.builder()
                .date(date)
                .build();
        TournamentGroup tournamentGroup = TournamentGroup.builder()
                .tournament(tournament)
                        .build();
        TournamentGroup tournamentGroup1 = TournamentGroup.builder()
                .tournament(tournament)
                        .build();
        List<TournamentGroup> groupList = new ArrayList<>();
        groupList.add(tournamentGroup);
        groupList.add(tournamentGroup1);
        tournament.setTournamentGroups(groupList);

        tournamentRepository.save(tournament);
        Tournament foundTournament = tournamentRepository.findAllGroupsByTournamentId(tournament.getId());

        Assertions.assertThat(foundTournament)
                .isNotNull();
        Assertions.assertThat(foundTournament.getTournamentGroups().size())
                .isEqualTo(2);
    }
}
