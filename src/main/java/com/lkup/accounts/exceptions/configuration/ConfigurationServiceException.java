package com.lkup.accounts.exceptions.configuration;

public class ConfigurationServiceException extends RuntimeException {

    public ConfigurationServiceException(String message) {
        super(message);
    }

    public ConfigurationServiceException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public ConfigurationServiceException(Throwable cause) {
        super(cause);
    }

    public ConfigurationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConfigurationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
