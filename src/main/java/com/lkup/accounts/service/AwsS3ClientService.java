package com.lkup.accounts.service;

import com.lkup.accounts.config.s3.AmazonS3Config;
import com.lkup.accounts.config.s3.AwsProperties;
import com.lkup.accounts.dto.s3.S3Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class AwsS3ClientService {

    private final S3Client s3Client;

    private final Environment env;

    public AwsS3ClientService(Environment env, @Qualifier("s3Client") S3Client s3Client) {
        this.s3Client = s3Client;
        this.env = env;
    }

    public S3Config getS3Config(String environmentType) {
        String prefix = "aws.credentials." + environmentType.toLowerCase() + ".";

        String accessKey = env.getProperty(prefix + "access-key");
        String secretKey = env.getProperty(prefix + "secret-key");
        String bucket = env.getProperty(prefix + "bucket");
        String directory = env.getProperty(prefix + "bucket.directory");
        String cdn = env.getProperty(prefix + "cdn");

        if (accessKey == null || secretKey == null || bucket == null) {
            throw new IllegalArgumentException("Invalid environment type or missing configuration for: " + environmentType);
        }

        return S3Config.builder().accessKey(accessKey).secretKey(secretKey).bucket(bucket).directory(directory).cdn(cdn).build();
    }


    public S3Client createS3Client(String accessKey, String secretKey, String region) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}
