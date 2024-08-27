package com.lkup.accounts.exceptions;

public class InvalidTeamIdException extends RuntimeException {
    public InvalidTeamIdException() {
        super();
    }

    public InvalidTeamIdException(String message) {
        super(message);
    }

    public InvalidTeamIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTeamIdException(Throwable cause) {
        super(cause);
    }

    protected InvalidTeamIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
