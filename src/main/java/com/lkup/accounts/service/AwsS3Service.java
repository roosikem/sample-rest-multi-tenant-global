package com.lkup.accounts.service;


import com.lkup.accounts.config.s3.AwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Objects;


@Slf4j
@Service
public class AwsS3Service {

    private final AwsProperties awsProperties;
    private final AwsS3ClientService awsS3ClientService;

    public AwsS3Service(AwsS3ClientService awsS3ClientService, AwsProperties awsProperties) {
        this.awsS3ClientService = awsS3ClientService;
        this.awsProperties = awsProperties;
    }

    public String uploadJsonData(String key, String jsonData) {
        S3Client s3Client = awsS3ClientService.getS3Client("config");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getBucket() )
                .key(key)
                .contentType("application/json")
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(jsonData.getBytes()));

        return key;
    }

    public String copyObject(String env, String sourceKey, String destinationKey) {
        S3Client s3Client = awsS3ClientService.getS3Client(env);
        CopyObjectRequest copyObjectRequest = CopyObjectRequest
                .builder().sourceBucket(awsProperties.getBucket(env))
                .sourceKey("")
                .destinationBucket("")
                .destinationKey("")
                .build();
        CopyObjectResponse copyObjectResponse = s3Client.copyObject(copyObjectRequest);
        return destinationKey;
    }
}
