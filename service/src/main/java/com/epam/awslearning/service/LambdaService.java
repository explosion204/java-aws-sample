package com.epam.awslearning.service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LambdaService {
    private final AWSLambda awsLambda;

    @Value("${aws.lambda.arn}")
    private String lambdaArn;

    public void invokeNotificationLambda() {
        final InvokeRequest request = new InvokeRequest()
                .withFunctionName(lambdaArn)
                .withPayload("{\"detail-type\": \"awslearning\"}");

        awsLambda.invoke(request);
    }
}
