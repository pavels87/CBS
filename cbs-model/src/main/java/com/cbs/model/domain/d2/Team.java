package com.cbs.model.domain.d2;

import com.cbs.model.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: PSpiridonov
 * Date: 01.07.15
 * Time: 11:29
 */
@Entity
@Table(name = "team")
@GenericGenerator(name = "seq_team_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_team_id"))
public class Team implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_team_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "steam_team_id", nullable = false, unique = true)
    private Long steamTeamId;

    @Column(name = "name")
    private String name;

    @Column(name = "tag", nullable = false)
    private String tag;

    //TODO разобраться с лого
    @Column(name = "logo")
    private String logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    //TODO нужно ли?
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "team_id")
    private Set<PlayerInfo> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSteamTeamId() {
        return steamTeamId;
    }

    public void setSteamTeamId(Long steamTeamId) {
        this.steamTeamId = steamTeamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Set<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerInfo> players) {
        this.players = players;
    }
}
