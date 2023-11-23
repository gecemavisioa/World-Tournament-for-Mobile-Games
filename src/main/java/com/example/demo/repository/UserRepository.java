package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Achieving FetchType.EAGER on User.pastRanks
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.pastRanks " +
            "WHERE u.id = :userId")
    Optional<User> findAllRanksByUserId(@Param("userId") int userId);
}
