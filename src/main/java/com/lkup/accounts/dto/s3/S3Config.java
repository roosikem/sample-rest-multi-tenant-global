package com.lkup.accounts.dto.s3;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class S3Config {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
    private String cdn;
    private String directory;
}
