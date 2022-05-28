package com.epam.awslearning.mq;

import com.epam.awslearning.service.NotificationService;
import lombok.RequiredArgsConstructor;
import com.amazonaws.services.sqs.model.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationQueueListener {
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 3000)
    public void readBatchFromQueueAndPushToTopic() {
        final List<Message> messages = notificationService.readMessages();
        messages.forEach(message -> notificationService.pushNotification(message.getBody()));
    }
}