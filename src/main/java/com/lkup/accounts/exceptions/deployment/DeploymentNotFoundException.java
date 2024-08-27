package com.lkup.accounts.exceptions.deployment;

public class DeploymentNotFoundException extends RuntimeException {

    public DeploymentNotFoundException(String message) {
        super(message);
    }
}
