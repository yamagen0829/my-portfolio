package com.example.nagoyameshi;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException(String message) {
        super(message);
    }
}
