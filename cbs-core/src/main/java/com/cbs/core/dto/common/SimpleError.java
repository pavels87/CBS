package com.cbs.core.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SimpleError {
    private String message;
    private String code;

    public SimpleError() {
    }

    public static SimpleError message(String message){
        SimpleError error = new SimpleError();
        error.setMessage(message);
        return error;
    }

    public static SimpleError code(String code){
        SimpleError error = new SimpleError();
        error.setCode(code);
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
