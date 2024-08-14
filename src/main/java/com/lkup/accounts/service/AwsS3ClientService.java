package com.lkup.accounts.service;

import com.lkup.accounts.config.s3.AmazonS3Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class AwsS3ClientService {

    private final S3Client s3Client;
    private final S3Client stagingS3Client;
    private final S3Client sandBoxS3Client;
    private final S3Client prodS3Client;


    public AwsS3ClientService(@Qualifier("s3Client") S3Client s3Client, @Qualifier("s3ClientStaging") S3Client stagingS3Client,
                              @Qualifier("s3ClientSandbox") S3Client sandBoxS3Client, @Qualifier("s3ClientProd") S3Client prodS3Client) {
        this.s3Client = s3Client;
        this.stagingS3Client = stagingS3Client;
        this.sandBoxS3Client = sandBoxS3Client;
        this.prodS3Client = prodS3Client;
    }

    public S3Client getS3Client(String env) {
        switch (env.toLowerCase()) {
            case "config":
                return s3Client;
            case "staging":
                return stagingS3Client;
            case "sandbox":
                return sandBoxS3Client;
            case "prod":
                return prodS3Client;
            default:
                throw new IllegalArgumentException("Unknown S3 environment");
        }
    }
}
