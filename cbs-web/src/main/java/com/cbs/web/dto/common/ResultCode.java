package com.cbs.web.dto.common;

public enum ResultCode {
    OK(200),
    PARTIAL_SUCCESS(200),
    EXTRA_INFO_REQUIRED(206),

    INVALID_CONTEXT(412),
    ENTITY_NOT_FOUND(404),
    ENTITY_ALREADY_EXIST(409),
    ENTITY_IS_USED(409),
    VALIDATION_ERROR(400),

    INSUFFICIENT_PERMISSIONS(403),
    OPERATION_ALREADY_DONE(409),

    PAGE_NOT_FOUND(404),
    BAD_REQUEST(400),
    UNCLASSIFIED_ERROR(500);

    private int httpCode;

    ResultCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
