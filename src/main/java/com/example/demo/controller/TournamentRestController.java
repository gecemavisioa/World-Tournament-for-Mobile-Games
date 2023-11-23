package com.example.demo.controller;

import com.example.demo.dto.CountryLeaderboard.CountryLeaderboard;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tournament")
public class TournamentRestController {
    private TournamentService tournamentService;

    @Autowired
    public TournamentRestController(TournamentService theTournamentService) {
        tournamentService = theTournamentService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<GroupLeaderboard> enterTournament(@PathVariable int userId) {
        if(!tournamentService.isActive()) throw new ForbiddenException("There is no tournament going on!");
        return new ResponseEntity<>(tournamentService.enterTournament(userId), HttpStatus.OK);
    }


    @GetMapping("/countryLeaderboard")
    public ResponseEntity<CountryLeaderboard> getCountryLeaderboard(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "0") int size) {
        return new ResponseEntity<>(tournamentService.getCountryLeaderboard(page, size), HttpStatus.OK);
    }


}
