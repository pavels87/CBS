package com.contmatic.g5.phoenix.domain;

import java.io.Serializable;

/**
 * @author DLevin
 * @since 06.11.2014
 */
public interface Identifiable<ID extends Serializable> {
    ID getId();

    void setId(ID id);
}

