package com.example.demo.repository;

import com.example.demo.entity.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Integer> {

    // Achieving FetchType.EAGER on TournamentGroup.groupUsers
    @Query("SELECT tg FROM TournamentGroup tg " +
            "LEFT JOIN FETCH tg.groupUsers gu " +
            "WHERE tg.id = :tournamentGroupId " +
            "ORDER BY gu.score DESC")
    TournamentGroup findAllGroupUsersById(@Param("tournamentGroupId") int tournamentGroupId);
}
