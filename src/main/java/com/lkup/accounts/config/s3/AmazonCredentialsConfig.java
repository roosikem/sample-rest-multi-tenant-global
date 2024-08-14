package com.lkup.accounts.config.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
@RequiredArgsConstructor
public class AmazonCredentialsConfig {

    private final AwsProperties awsProperties;

    @Bean("awsCredentials")
    public AwsCredentialsProvider awsCredentials() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(awsProperties.getAccess(),
                awsProperties.getSecret()));
    }
}
