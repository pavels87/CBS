package com.cbs.core.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.Set;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ValidationError {
    private String property;
    private Set<CategorizedHintError> errorItems;

    public ValidationError() {
    }

    public ValidationError(CategorizedHintError errorItem) {
        this(null, Collections.singleton(errorItem));
    }

    public ValidationError(Set<CategorizedHintError> errorItems) {
        this(null, errorItems);
    }

    public ValidationError(String property, Set<CategorizedHintError> errorItems) {
        this.property = property;
        this.errorItems = errorItems;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Set<CategorizedHintError> getErrorItems() {
        return errorItems;
    }

    public void setErrorItems(Set<CategorizedHintError> errorItems) {
        this.errorItems = errorItems;
    }
}
