package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.dto.GroupLeaderboard.GroupLeaderboard;
import com.example.demo.dto.GroupRank.GroupRanks;
import org.springframework.http.HttpStatus;
import com.example.demo.service.TournamentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private TournamentService tournamentService;
    private UserService userService;

    @Autowired
    public UserRestController(UserService theUserService, TournamentService theTournamentService) {
        tournamentService = theTournamentService;
        userService = theUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user.getUsername()), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> proceedLevel(@PathVariable int userId) {
        User user = userService.proceedLevel(userId);

        if(tournamentService.isActive()){
            tournamentService.incrementUserTournamentScore(userId);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GroupLeaderboard> getGroupLeaderboard(@PathVariable int userId) {
        return new ResponseEntity<>(tournamentService.getGroupLeaderboard(userId), HttpStatus.OK);
    }

    @PutMapping("/receiveReward/{userId}")
    public ResponseEntity<User> receiveReward(@PathVariable int userId) {
        return new ResponseEntity<>(userService.receiveReward(userId), HttpStatus.OK);
    }

    @GetMapping("/pastRanks/{userId}")
    public ResponseEntity<GroupRanks> getPastRanks(@PathVariable int userId) {
        return new ResponseEntity<>(userService.findPastRanksByUserId(userId), HttpStatus.OK) ;
    }
}
