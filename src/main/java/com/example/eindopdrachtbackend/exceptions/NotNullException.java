package com.example.eindopdrachtbackend.exceptions;

public class NotNullException extends RuntimeException {
    public NotNullException() {
        super("Value cannot be null");
    }

    public NotNullException(String message) {
        super(message);
    }
}
