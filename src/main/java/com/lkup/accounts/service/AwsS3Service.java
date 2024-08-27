package com.lkup.accounts.service;


import com.lkup.accounts.config.s3.AwsProperties;
import com.lkup.accounts.document.Environment;
import com.lkup.accounts.dto.s3.S3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

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


    public String uploadJsonData(String key, String jsonData, Environment environment, boolean isOverriding) {
        Assert.notNull(environment.getEnvironmentType(), "Environment Type can not be blank");
        S3Config config = awsS3ClientService.getS3Config(environment.getEnvironmentType());
        if (!isOverriding && Objects.nonNull(config.getDirectory()) && !config.getDirectory().isEmpty()) {
            key = config.getDirectory() + "/" + key;;
        }
        S3Client s3Client = buildS3Client(config);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(config.getBucket())
                .key(key)
                .contentType("application/json")
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(jsonData.getBytes()));

        return key;
    }

    public S3Client buildS3Client(S3Config config) {
        Assert.notNull(config, "Aws S3 Configuration can not be null");
        S3Client s3Client = awsS3ClientService.createS3Client(config.getAccessKey(), config.getSecretKey(), config.getRegion());
        return s3Client;
    }

    public String copyObject(String env, String sourceKey, String destinationKey) {
        S3Config config = awsS3ClientService.getS3Config(env);
        S3Client s3Client = awsS3ClientService.createS3Client(config.getAccessKey(), config.getSecretKey(), config.getRegion());
        CopyObjectRequest copyObjectRequest = CopyObjectRequest
                .builder().sourceBucket(config.getBucket())
                .sourceKey("")
                .destinationBucket("")
                .destinationKey("")
                .build();
        CopyObjectResponse copyObjectResponse = s3Client.copyObject(copyObjectRequest);
        return destinationKey;
    }
}
