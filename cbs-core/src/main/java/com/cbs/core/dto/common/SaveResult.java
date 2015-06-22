package com.cbs.core.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SaveResult<K> {

    private K id;

    private List<ValidationError> errors;

    private Boolean canBeSaved;

    public SaveResult() {
    }

    public SaveResult(K id, List<ValidationError> errors) {
        this.id = id;
        this.errors = errors;
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public Boolean getCanBeSaved() {
        return canBeSaved;
    }

    public void setCanBeSaved(Boolean canBeSaved) {
        this.canBeSaved = canBeSaved;
    }
}
