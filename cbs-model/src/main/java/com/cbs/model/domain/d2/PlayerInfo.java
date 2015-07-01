package com.cbs.model.domain.d2;

import com.cbs.model.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 01.07.15
 * Time: 11:21
 */
@Entity
@Table(name = "player_info")
@GenericGenerator(name = "seq_player_info_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_player_info_id"))
public class PlayerInfo implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_player_info_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private Long accountId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfo that = (PlayerInfo) o;

        if (!accountId.equals(that.accountId)) return false;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return team.equals(that.team);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + accountId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", name=" + name +
                ", team=" + team +
                '}';
    }
}
