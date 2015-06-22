package com.cbs.domain;

import com.contmatic.g5.phoenix.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 15:12
 */
/*
Represents information about players in teams
 */
@Entity
@Table(name = "players_2_teams")
@GenericGenerator(name = "seq_players_2_teams_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_players_2_teams_id"))
public class Players2Teams implements Identifiable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_players_2_teams_generator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @NotNull
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @NotNull
    private Team team;

    @Column(name = "joined_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant joinedDate;

    @Column(name = "left_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant leftDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
