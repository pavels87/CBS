package com.cbs.model.domain.d2;

import com.cbs.model.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 01.07.15
 * Time: 11:39
 */
@Entity
@Table(name = "league")
@GenericGenerator(name = "seq_league_generator",
        strategy = "sequence",
        parameters = @Parameter(name = "sequence", value = "seq_league_id"))
public class League implements Identifiable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_league_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "steam_league_id", nullable = false, unique = true)
    private Long steamLeagueId;

    @Column(name = "name", nullable = false)
    private String name;

    //TODO разобраться с лого
    @Column(name = "logo")
    private String logo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSteamLeagueId() {
        return steamLeagueId;
    }

    public void setSteamLeagueId(Long steamLeagueId) {
        this.steamLeagueId = steamLeagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        League league = (League) o;

        if (!id.equals(league.id)) return false;
        if (!logo.equals(league.logo)) return false;
        if (!name.equals(league.name)) return false;
        if (!steamLeagueId.equals(league.steamLeagueId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + steamLeagueId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + logo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", steamLeagueId=" + steamLeagueId +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
