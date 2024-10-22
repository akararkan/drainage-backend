package com.ak.drainage.user.exceptions;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String message) {
        super(message);
    }

    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}

