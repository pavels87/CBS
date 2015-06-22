package com.cbs.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 14:57
 */
@Entity
@DiscriminatorValue(value = "SINGLE")
public class Player extends Participant {

    @Column(name = "real_name")
    private String realName;

}
