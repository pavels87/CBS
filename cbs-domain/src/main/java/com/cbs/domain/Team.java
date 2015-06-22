package com.cbs.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * User: PSpiridonov
 * Date: 22.06.15
 * Time: 14:56
 */
@Entity
@DiscriminatorValue(value = "TEAM")
public class Team extends Participant{
}
