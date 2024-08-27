package com.lkup.accounts.exceptions;

public class InvalidTenantIdException extends RuntimeException {
    public InvalidTenantIdException() {
        super();
    }

    public InvalidTenantIdException(String message) {
        super(message);
    }

    public InvalidTenantIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTenantIdException(Throwable cause) {
        super(cause);
    }

    protected InvalidTenantIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
