package com.example.eindopdrachtbackend.exceptions;

public class FriendshipAlreadyExistsException extends RuntimeException {
    public FriendshipAlreadyExistsException() {
        super("Friendship already exists.");
    }

    public FriendshipAlreadyExistsException(String message) {
        super(message);
    }
}
