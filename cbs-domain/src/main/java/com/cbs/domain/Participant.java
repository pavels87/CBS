package com.cbs.domain;

import com.cbs.enums.Country;
import com.cbs.enums.GameType;
import com.cbs.enums.ParticipantType;
import com.contmatic.g5.phoenix.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 14:43
 */
/*
Participant of the match (player or team)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "participant_type",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue(value = "INCORRECT")
@Table(name = "participant")
@GenericGenerator(name = "seq_participant_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_participant_id"))
public abstract class Participant implements Identifiable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_participant_generator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", insertable = false, updatable = false)
    private ParticipantType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private Country country;

    @Column(name = "logo")
    private byte[] logo;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParticipantType getType() {
        return type;
    }

    public void setType(ParticipantType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
