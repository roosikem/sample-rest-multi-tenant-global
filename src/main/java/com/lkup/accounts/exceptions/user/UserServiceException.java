package com.lkup.accounts.exceptions.user;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message, String eMessage) {
        super(message);
    }
}
