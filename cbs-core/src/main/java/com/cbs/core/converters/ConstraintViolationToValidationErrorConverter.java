package com.cbs.core.converters;

import com.cbs.core.dto.common.CategorizedHintError;
import com.cbs.core.dto.common.ValidationError;
import com.cbs.core.utils.helpers.ValidationErrorContainer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

@Component
public class ConstraintViolationToValidationErrorConverter<E> implements Converter<Set<ConstraintViolation<E>>, List<ValidationError>> {

    @Override
    public List<ValidationError> convert(Set<ConstraintViolation<E>> violations) {
        if (violations == null) return null;
        final ValidationErrorContainer validationErrorContainer = new ValidationErrorContainer();
        violations.stream().forEach(v -> {
            validationErrorContainer.addError(String.valueOf(v.getPropertyPath()), CategorizedHintError.message(v.getMessage()));
        });
        return validationErrorContainer.getValidationErrorsAsList();
    }
}
