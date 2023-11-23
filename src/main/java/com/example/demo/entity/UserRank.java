package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user_rank")
public class UserRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "tournament_date")
    private ZonedDateTime tournamentDate;

    @Column(name="user_rank")
    private int rank;

    public UserRank(ZonedDateTime tournamentDate, int rank) {
        this.tournamentDate = tournamentDate;
        this.rank = rank;
    }
}
