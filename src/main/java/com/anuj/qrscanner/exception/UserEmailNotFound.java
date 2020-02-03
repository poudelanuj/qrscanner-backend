package com.anuj.qrscanner.exception;

public class UserEmailNotFound extends RuntimeException {
    public UserEmailNotFound(String email) {
        super("Couldn't find user with email : " + email);
    }
}
