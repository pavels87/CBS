package com.cbs.domain;

import com.contmatic.g5.phoenix.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 14:29
 */

/*
Represents match between 2 players/commands
 */
@Entity
@Table(name = "match")
@GenericGenerator(name = "seq_match_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_match_id"))
public class Match implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_match_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_match")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant dateOfMatch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
