package com.example.eindopdrachtbackend.exceptions;

public class NameTooLongException extends RuntimeException {
    public NameTooLongException() {
        super("Name too long");
    }

    public NameTooLongException(String message) {
        super(message);
    }
}

