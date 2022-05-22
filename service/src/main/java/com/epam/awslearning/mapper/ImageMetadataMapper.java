package com.epam.awslearning.mapper;

import com.epam.awslearning.dto.ImageUploadDto;
import com.epam.awslearning.model.entity.ImageMetadata;
import com.epam.awslearning.dto.ImageMetadataDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMetadataMapper {
    ImageMetadataDto toImageMetadataDto(ImageMetadata imageMetadata);
    ImageMetadata toImageMetadata(ImageUploadDto imageUploadDto);
}
