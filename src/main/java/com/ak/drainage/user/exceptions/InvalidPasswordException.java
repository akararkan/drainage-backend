package com.ak.drainage.user.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message) {
        super(message);
    }
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
