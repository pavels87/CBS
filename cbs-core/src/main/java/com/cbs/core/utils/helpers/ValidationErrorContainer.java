package com.cbs.core.utils.helpers;

import com.cbs.core.dto.common.CategorizedHintError;
import com.cbs.core.dto.common.ValidationError;

import java.util.*;
import java.util.stream.Collectors;

public class ValidationErrorContainer {

    private Map<String, Set<CategorizedHintError>> propertyToErrorItems = new HashMap<>();

    public void addError(String property, CategorizedHintError errorItem) {
        final Set<CategorizedHintError> errorItems = propertyToErrorItems.getOrDefault(property, new HashSet<>());

        errorItems.add(errorItem);

        propertyToErrorItems.put(property, errorItems);
    }

    public void addError(String property, Set<CategorizedHintError> errorItems) {
        final Set<CategorizedHintError> propErrorItems = propertyToErrorItems.getOrDefault(property, new HashSet<>());

        propErrorItems.addAll(errorItems);

        propertyToErrorItems.put(property, propErrorItems);
    }

    public List<ValidationError> getValidationErrorsAsList() {
        return propertyToErrorItems.entrySet().stream()
                .map(entry -> new ValidationError(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
