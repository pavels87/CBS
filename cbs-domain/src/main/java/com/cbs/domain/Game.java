package com.cbs.domain;

import com.contmatic.g5.phoenix.domain.Identifiable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 14:32
 */
/*
Represents single game from specific match
 */
@Entity
@Table(name = "game")
@GenericGenerator(name = "seq_game_generator",
        strategy = "sequence",
        parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "seq_game_id"))
public class Game implements Identifiable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_game_generator")
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
