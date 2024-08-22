package com.lkup.accounts.config.s3;

import com.lkup.accounts.enums.EnvironmentType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;
import static com.lkup.accounts.utilities.AwsConstant.*;

@Data
@Component
@ConfigurationProperties("spring.cloud.aws")
public class AwsProperties {
    @Value("${spring.cloud.aws.region.static}")
    private String region;
    @Value("${spring.cloud.aws.credentials.access-key}")
    private String access;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secret;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.cdn}")
    private String cdn;

    @Value("${spring.cloud.aws.s3.bucket.directory}")
    private String directory;

    @Value("${aws.region.staging.static}")
    private String stagingRegion;

    @Value("${aws.credentials.staging.access-key}")
    private String stagingAccess;

    @Value("${aws.credentials.staging.secret-key}")
    private String stagingSecret;

    @Value("${aws.s3.staging.bucket}")
    private String stagingBucket;

    @Value("${aws.region.sandbox.static}")
    private String sandboxRegion;

    @Value("${aws.credentials.sandbox.access-key}")
    private String sandboxAccess;

    @Value("${aws.credentials.sandbox.secret-key}")
    private String sandboxSecret;

    @Value("${aws.s3.sandbox.bucket}")
    private String sandboxBucket;

    @Value("${aws.region.prod.static}")
    private String prodRegion;

    @Value("${aws.credentials.prod.access-key}")
    private String prodAccess;

    @Value("${aws.credentials.prod.secret-key}")
    private String prodSecret;

    @Value("${aws.s3.prod.bucket}")
    private String prodBucket;



    public String getBucket(EnvironmentType env) {
        Objects.requireNonNull(env);
        return switch (env) {
            case DEV -> bucket;
            case PROD -> prodBucket;
            case SANDBOX -> sandboxBucket;
            case STAGING -> stagingBucket;
            case QA -> stagingBucket;
            case PRE_PROD -> stagingBucket;
            default -> throw new IllegalArgumentException("Unknown environment " + env);
        };
    }

    public String getAccessKey(EnvironmentType env) {
        Objects.requireNonNull(env);
        return switch (env) {
            case DEV -> access;
            case PROD -> prodAccess;
            case SANDBOX -> sandboxAccess;
            case STAGING -> stagingAccess;
            case QA -> stagingAccess;
            case PRE_PROD -> stagingAccess;
            default -> throw new IllegalArgumentException("Unknown environment " + env);
        };
    }

    public String getSecret(EnvironmentType env) {
        Objects.requireNonNull(env);
        return switch (env) {
            case DEV -> secret;
            case PROD -> prodSecret;
            case SANDBOX -> sandboxSecret;
            case STAGING -> stagingSecret;
            case QA -> stagingSecret;
            case PRE_PROD -> stagingSecret;
            default -> throw new IllegalArgumentException("Unknown environment " + env);
        };
    }

}
