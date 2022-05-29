package com.epam.awslearning.controller;

import com.epam.awslearning.service.LambdaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lambda")
@RequiredArgsConstructor
public class LambdaController {
    private final LambdaService lambdaService;

    @PostMapping
    public ResponseEntity<Void> invokeLambda() {
        lambdaService.invokeNotificationLambda();
        return ResponseEntity.noContent().build();
    }
}
