package com.example.demo.repository;

import com.example.demo.entity.GroupUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Integer> {
    @Query("SELECT gu FROM GroupUser gu " +
            "WHERE (gu.user.id = :data AND gu.group.tournament.id = :data2)")
    Optional<GroupUser> findByUserIdAndTournamentId(@Param("data") int userId, @Param("data2") int tournamentId);

    // Returns List of GroupUsers
    // Who has the highest scores from each country
    // Limited by Pageable
    @Query("SELECT gu FROM GroupUser gu " +
            "WHERE (gu.group.tournament.id = :tournamentId AND gu.user.country = :country)" +
            "ORDER BY gu.score DESC")
    List<GroupUser> findAllUsersByTournamentId(@Param("tournamentId") int tournamentId, @Param("country") String country, Pageable pageable);


    // Returns a country's sum of scores
    @Query("SELECT SUM(gu.score) FROM GroupUser gu " +
            "WHERE gu.group.tournament.id = :tournamentId AND gu.user.country = :country")
    Long findTotalScoreByTournamentIdAndCountry(@Param("tournamentId") int tournamentId, @Param("country") String country);

}
