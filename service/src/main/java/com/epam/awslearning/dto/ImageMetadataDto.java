package com.epam.awslearning.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageMetadataDto {
    private long id;
    private String name;
    private long size;
    private String fileExtension;
    private LocalDateTime lastUpdated;
}
