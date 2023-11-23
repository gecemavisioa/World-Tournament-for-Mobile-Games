package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tournament_group")
public class TournamentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<GroupUser> groupUsers;

    // Setting GroupUsers
    // When TournamentGroup saved CascadeType.PERSIST also saves groupUsers
    public void setGroupUsers(List<GroupUser> groupUsers) {
        for(GroupUser gu: groupUsers) {
            gu.setGroup(this);
        }
        this.groupUsers = groupUsers;
    }
}
