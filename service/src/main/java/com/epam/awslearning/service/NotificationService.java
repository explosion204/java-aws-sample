package com.epam.awslearning.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final AmazonSNS sns;
    private final AmazonSQS sqs;

    @Value("${aws.sqs.queue.url}")
    private String queueUrl;

    @Value("${aws.sqs.queue.throughput}")
    private int readingThroughput;

    @Value("${aws.sns.topic.arn}")
    private String topicArn;

    @Value("${aws.sns.topic.protocol}")
    private String topicProtocol;

    public void enqueueMessage(String message) {
        final SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        sqs.sendMessage(request);
    }

    public void pushNotification(String message) {
        final PublishRequest publishRequest = new PublishRequest()
                .withMessage(message)
                .withTopicArn(topicArn);

        sns.publish(publishRequest);
    }

    public List<Message> readMessages() {
        final ReceiveMessageRequest request = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withMaxNumberOfMessages(readingThroughput);

       final List<Message> messages = sqs.receiveMessage(request).getMessages();

       messages.stream()
               .map(Message::getReceiptHandle)
               .forEach(receipt -> sqs.deleteMessage(queueUrl, receipt));

       return messages;
    }

    public void subscribeEmail(String email) {
        final SubscribeRequest request = new SubscribeRequest()
                .withProtocol(topicProtocol)
                .withEndpoint(email)
                .withTopicArn(topicArn);

        sns.subscribe(request);
    }

    public void unsubscribeEmail(String email) {
        sns.listSubscriptionsByTopic(topicArn)
                .getSubscriptions()
                .stream()
                .filter(subscription -> email.equals(subscription.getEndpoint()))
                .findAny()
                .ifPresent(subscription -> removeSubscription(subscription.getSubscriptionArn()));
    }

    private void removeSubscription(String subscriptionArn) {
        final UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest()
                .withSubscriptionArn(subscriptionArn);

        sns.unsubscribe(unsubscribeRequest);
    }
}
