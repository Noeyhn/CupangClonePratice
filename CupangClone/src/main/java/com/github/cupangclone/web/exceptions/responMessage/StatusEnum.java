package com.github.cupangclone.web.exceptions.responMessage;

public enum StatusEnum {

    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    UNAUTHORIZED(401, "UNAUTHORIZED");

    final int statusCode;
    final String codeName;

    StatusEnum(int statusCode, String codeName) {
        this.statusCode = statusCode;
        this.codeName = codeName;
    }
}
