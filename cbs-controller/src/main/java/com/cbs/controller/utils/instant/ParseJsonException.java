package com.cbs.controller.utils.instant;

/**
 * User: PSpiridonov
 * Date: 26.05.15
 * Time: 14:05
 */
public class ParseJsonException extends RuntimeException{

    private String fieldName;
    private String code;
    private String hint;

    public ParseJsonException(String fieldName, String code, String hint) {
        this.fieldName = fieldName;
        this.code = code;
        this.hint = hint;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getCode() {
        return code;
    }

    public String getHint() {
        return hint;
    }
}
