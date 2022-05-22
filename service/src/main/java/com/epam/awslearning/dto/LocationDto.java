package com.epam.awslearning.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationDto {
    private String region;
    private String availabilityZone;
}
