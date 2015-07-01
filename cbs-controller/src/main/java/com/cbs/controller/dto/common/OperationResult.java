package com.cbs.controller.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OperationResult<T> {
    private ResultCode code;
    private T result;

    public OperationResult() {
    }

    public OperationResult(ResultCode code, T result) {
        this.code = code;
        this.result = result;
    }

    public OperationResult(ResultCode code) {
        this(code, null);
    }

    public static <T> OperationResult<T> ok() {
        return new OperationResult<>(ResultCode.OK);
    }

    public static <T> OperationResult<T> ok(T result) {
        return new OperationResult<>(ResultCode.OK, result);
    }

    @JsonIgnore
    public boolean isOk() {
        return ResultCode.OK.equals(code);
    }

    public <T2> OperationResult<T2> withResult(T2 result) {
        return new OperationResult<>(this.getCode(), result);
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}