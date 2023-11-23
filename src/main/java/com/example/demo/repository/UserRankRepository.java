package com.example.demo.repository;

import com.example.demo.entity.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRankRepository extends JpaRepository<UserRank, Integer> {
}
