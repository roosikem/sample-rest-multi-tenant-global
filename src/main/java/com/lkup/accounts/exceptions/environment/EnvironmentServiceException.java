package com.lkup.accounts.exceptions.environment;

public class EnvironmentServiceException extends RuntimeException {
    public EnvironmentServiceException(String errorCreatingEnvironment, String message) {
        super(errorCreatingEnvironment, new Throwable(message));
    }
}
