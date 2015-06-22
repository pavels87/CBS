package com.cbs.core.dto.common;

import com.cbs.core.dto.enums.ResultCode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorAwareOperationResult<T, E> extends OperationResult<T> {
    private E errors;

    public ErrorAwareOperationResult() {
    }

    public ErrorAwareOperationResult(ResultCode code, T result, E errors) {
        super(code, result);
        this.errors = errors;
    }

    public E getErrors() {
        return errors;
    }

    public void setErrors(E errors) {
        this.errors = errors;
    }

    public <T2> ErrorAwareOperationResult<T2, E> withResult(T2 result) {
        return new ErrorAwareOperationResult<>(this.getCode(), result, this.getErrors());
    }

}
