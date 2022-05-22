package com.epam.awslearning.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadDto {
    private String name;
    private MultipartFile file;
}
