package com.example.eindopdrachtbackend.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {
        super("Record not found.");
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
