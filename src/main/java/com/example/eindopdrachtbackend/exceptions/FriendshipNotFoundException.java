package com.example.eindopdrachtbackend.exceptions;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException() {
        super("Friendship not found.");
    }

    public FriendshipNotFoundException(String message) {
        super(message);
    }
}
