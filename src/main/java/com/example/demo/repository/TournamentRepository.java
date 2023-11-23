package com.example.demo.repository;

import com.example.demo.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    @Query("SELECT t FROM Tournament t WHERE YEAR(t.date) = :year " +
            "AND MONTH(t.date) = :month " +
            "AND DAY(t.date) = :day")
    Tournament findByDate(@Param("year") int year,
                          @Param("month") int month,
                          @Param("day") int day);

    // Achieving FetchType.EAGER on Tournament.tournamentGroups
    @Query("SELECT t FROM Tournament t " +
            "LEFT JOIN FETCH t.tournamentGroups " +
            "WHERE t.id = :tournamentId ")
    Tournament findAllGroupsByTournamentId(@Param("tournamentId") int tournamentId);

}
