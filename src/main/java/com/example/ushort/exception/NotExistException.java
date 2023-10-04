package com.example.ushort.exception;

public class NotExistException extends Exception {
    private String message;

    public NotExistException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
