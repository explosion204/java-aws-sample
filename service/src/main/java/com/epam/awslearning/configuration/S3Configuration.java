package com.epam.awslearning.configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {
    @Value("${s3.region}")
    private String region;

    @Bean
    public AmazonS3 s3() {
        return AmazonS3ClientBuilder.standard()
                 .withCredentials(new ProfileCredentialsProvider())
                .withRegion(region)
                .build();
    }
}
