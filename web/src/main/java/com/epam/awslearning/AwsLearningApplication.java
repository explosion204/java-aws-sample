package com.epam.awslearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AwsLearningApplication {
    public static void main(String[] args) {
        SpringApplication.run(AwsLearningApplication.class);
    }
}
