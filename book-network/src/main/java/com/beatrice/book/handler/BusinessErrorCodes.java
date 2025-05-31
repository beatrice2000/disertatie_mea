package com.beatrice.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    //pt procesarea parolei
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "The current password you entered is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "Your user account has been locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Your user account has been disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Invalid username or password"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
