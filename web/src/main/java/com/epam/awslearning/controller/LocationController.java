package com.epam.awslearning.controller;

import com.epam.awslearning.dto.LocationDto;
import com.epam.awslearning.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<LocationDto> getLocation() {
        final LocationDto locationDto = locationService.getLocation();
        return ResponseEntity.ok(locationDto);
    }
}
