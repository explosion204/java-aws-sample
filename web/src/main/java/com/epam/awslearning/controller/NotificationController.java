package com.epam.awslearning.controller;

import com.epam.awslearning.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PutMapping("/{email}")
    public ResponseEntity<Void> subscribeEmail(@PathVariable String email) {
        notificationService.subscribeEmail(email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> unsubscribeEmail(@PathVariable String email) {
        notificationService.unsubscribeEmail(email);
        return ResponseEntity.noContent().build();
    }

}
