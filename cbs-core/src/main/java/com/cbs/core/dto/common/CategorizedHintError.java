package com.cbs.core.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CategorizedHintError extends SimpleError {
    private ErrorType type = ErrorType.ERROR;
    private String hint;

    public CategorizedHintError() {
    }

    public static CategorizedHintError message(String message) {
        CategorizedHintError error = new CategorizedHintError();
        error.setMessage(message);
        return error;
    }

    public static CategorizedHintError message(String message, ErrorType type) {
        CategorizedHintError error = CategorizedHintError.message(message);
        error.setType(type);
        return error;
    }

    public static CategorizedHintError message(String message, ErrorType type, String hint) {
        CategorizedHintError error = CategorizedHintError.message(message, type);
        error.setHint(hint);
        return error;
    }

    public static CategorizedHintError code(String code) {
        CategorizedHintError error = new CategorizedHintError();
        error.setCode(code);
        return error;
    }

    public static CategorizedHintError code(String code, ErrorType type) {
        CategorizedHintError error = CategorizedHintError.code(code);
        error.setType(type);
        return error;
    }

    public static CategorizedHintError code(String code, ErrorType type, String hint) {
        CategorizedHintError error = CategorizedHintError.code(code, type);
        error.setHint(hint);
        return error;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
