package com.cbs.model.domain;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {
    ID getId();

    void setId(ID id);
}

