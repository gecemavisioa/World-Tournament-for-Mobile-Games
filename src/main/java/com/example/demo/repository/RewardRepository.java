package com.example.demo.repository;

import com.example.demo.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer> {
    Reward findByUserId(int id);
}
