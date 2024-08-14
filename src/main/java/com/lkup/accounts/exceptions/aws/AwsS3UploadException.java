package com.lkup.accounts.exceptions.aws;

public class AwsS3UploadException extends RuntimeException {

    public AwsS3UploadException(String message) {
        super(message);
    }

    public AwsS3UploadException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public AwsS3UploadException(Throwable cause) {
        super(cause);
    }

    public AwsS3UploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
