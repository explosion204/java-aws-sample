package com.epam.awslearning.dto;

import lombok.Builder;

@Builder
public class LocationDto {
    private String region;
    private String availabilityZone;
}
