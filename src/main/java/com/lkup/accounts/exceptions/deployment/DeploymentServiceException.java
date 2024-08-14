package com.lkup.accounts.exceptions.deployment;

public class DeploymentServiceException extends RuntimeException {

    public DeploymentServiceException(String message) {
        super(message);
    }

    public DeploymentServiceException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public DeploymentServiceException(Throwable cause) {
        super(cause);
    }

    public DeploymentServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
