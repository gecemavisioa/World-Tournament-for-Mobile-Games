package com.example.demo.dto.GroupRank;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GroupRank {
    private String tournamentDate;
    private int rank;

    public GroupRank(ZonedDateTime tournamentDate, int rank) {
        this.tournamentDate = tournamentDate.getYear() + "-" + tournamentDate.getMonthValue() + "-" + tournamentDate.getDayOfMonth();
        this.rank = rank;
    }
}
