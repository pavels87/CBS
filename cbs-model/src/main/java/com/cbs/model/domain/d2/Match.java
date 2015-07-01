package com.cbs.model.domain.d2;

import com.cbs.model.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 01.07.15
 * Time: 14:05
 */
@Entity
@Table(name = "match")
@GenericGenerator(name = "seq_match_generator",
        strategy = "sequence",
        parameters = @Parameter(name = "sequence", value = "seq_match_id"))
public class Match implements Identifiable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_match_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "steam_match_id", nullable = false, unique = true)
    private Long steamMatchId;

    @Column(name = "start_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dire_team_id", nullable = false)
    private Team direTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiant_team_id", nullable = false)
    private Team radiantTeam;

    //TODO мб пригодится
    @Column(name = "match_seq_num", nullable = false)
    private Long matchSeqNum;

    @Column(name = "radiant_win", nullable = false)
    private boolean radianWin;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    //TODO мб пригодится
    @Column(name = "dire_kills")
    private int direKills;

    //TODO мб пригодится
    @Column(name = "radiant_kills")
    private int radiantKills;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSteamMatchId() {
        return steamMatchId;
    }

    public void setSteamMatchId(Long steamMatchId) {
        this.steamMatchId = steamMatchId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Team getDireTeam() {
        return direTeam;
    }

    public void setDireTeam(Team direTeam) {
        this.direTeam = direTeam;
    }

    public Team getRadiantTeam() {
        return radiantTeam;
    }

    public void setRadiantTeam(Team radiantTeam) {
        this.radiantTeam = radiantTeam;
    }

    public Long getMatchSeqNum() {
        return matchSeqNum;
    }

    public void setMatchSeqNum(Long matchSeqNum) {
        this.matchSeqNum = matchSeqNum;
    }

    public boolean isRadianWin() {
        return radianWin;
    }

    public void setRadianWin(boolean radianWin) {
        this.radianWin = radianWin;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public int getDireKills() {
        return direKills;
    }

    public void setDireKills(int direKills) {
        this.direKills = direKills;
    }

    public int getRadiantKills() {
        return radiantKills;
    }

    public void setRadiantKills(int radiantKills) {
        this.radiantKills = radiantKills;
    }
}
