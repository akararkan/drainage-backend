package com.ak.drainage.user.exceptions;


public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException(String message) {
        super(message);
    }

    public ExpiredJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}