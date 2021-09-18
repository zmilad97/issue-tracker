package com.github.zmilad97.bugtracker.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String message) {
        super("User Already Exist ! \n" + message);
    }
}

