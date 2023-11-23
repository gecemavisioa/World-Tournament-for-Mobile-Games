package com.example.demo.entity;

import com.example.demo.util.CountryHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Version
    @Column(name="version")
    @JsonIgnore
    private long version = (long) 0;

    @Column(name="username")
    private String username;

    @Column(name="level")
    private int level = 1;

    @Column(name="coins")
    private int coins = 5000;

    @Column(name="country")
    private String country = CountryHelper.getRandomCountry();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    @JsonIgnore
    private List<UserRank> pastRanks;

    public User(String username) {
        this.username = username;
    }

    public User proceedLevel() {
        level += 1;
        coins += 25;
        return this;
    }

    public User receiveReward(int amount) {
        coins += amount;
        return this;
    }

    public User payTournamentFee() {
        coins -= 1000;
        return this;
    }
}
