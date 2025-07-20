package com.example.eindopdrachtbackend.exceptions;

public class CannotReviewTwiceException extends RuntimeException {
    public CannotReviewTwiceException() {
        super("You cannot review a game more than once");
    }

    public CannotReviewTwiceException(String message) {
        super(message);
    }
}
