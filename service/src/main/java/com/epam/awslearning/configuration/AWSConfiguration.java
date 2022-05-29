package com.epam.awslearning.configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {
    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 s3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(region)
                .build();
    }

    @Bean
    public AmazonSNS sns() {
        return AmazonSNSClient.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(region)
                .build();
    }

    @Bean
    public AmazonSQS sqs() {
        return AmazonSQSClient.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(region)
                .build();
    }
    
    @Bean
    public AWSLambda lambda() {
        return AWSLambdaClient.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(region)
                .build();
    }
}
