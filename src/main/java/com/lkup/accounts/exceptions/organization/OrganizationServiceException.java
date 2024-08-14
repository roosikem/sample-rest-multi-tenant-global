package com.lkup.accounts.exceptions.organization;

public class OrganizationServiceException extends RuntimeException {

    public OrganizationServiceException(String message) {
        super(message);
    }

    public OrganizationServiceException(String message, String cause) {
        super(message, new Throwable(cause));
    }
}
