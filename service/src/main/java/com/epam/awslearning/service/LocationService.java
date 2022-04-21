package com.epam.awslearning.service;

import com.amazonaws.util.EC2MetadataUtils;
import com.epam.awslearning.dto.LocationDto;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    public LocationDto getLocation() {
        return LocationDto.builder()
                .region(EC2MetadataUtils.getEC2InstanceRegion())
                .availabilityZone(EC2MetadataUtils.getAvailabilityZone())
                .build();
    }
}
