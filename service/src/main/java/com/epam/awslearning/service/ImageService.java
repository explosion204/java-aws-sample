package com.epam.awslearning.service;

import com.epam.awslearning.dto.ImageMetadataDto;
import com.epam.awslearning.dto.ImageUploadDto;
import com.epam.awslearning.exception.ServiceException;
import com.epam.awslearning.model.entity.ImageMetadata;
import com.epam.awslearning.model.repository.ImageMetadataRepository;
import com.epam.awslearning.mapper.ImageMetadataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private static final String FILE_EXTENSION_NOT_SPECIFIED = "File extension not specified";

    private final ImageMetadataRepository imageMetadataRepository;
    private final ImageMetadataMapper imageMetadataMapper;
    private final S3Service s3Service;

    public List<ImageMetadataDto> findAllMetadata() {
        return imageMetadataRepository.findAll()
                .stream()
                .map(imageMetadataMapper::toImageMetadataDto)
                .toList();
    }

    public ImageMetadataDto findRandomMetadata() {
        return imageMetadataRepository.findRandom()
                .map(imageMetadataMapper::toImageMetadataDto)
                .orElseThrow(() -> new ServiceException("No images found"));
    }

    public byte[] downloadImage(String name) {
        return s3Service.downloadObject(name);
    }

    @Transactional
    public ImageMetadataDto uploadImage(ImageUploadDto imageUploadDto) {
        final MultipartFile file = imageUploadDto.getFile();
        s3Service.uploadObject(file, imageUploadDto.getName());

        final ImageMetadata initialImage = imageMetadataMapper.toImageMetadata(imageUploadDto);
        fillImageMetadata(initialImage, file);

        final ImageMetadata savedImage = imageMetadataRepository.save(initialImage);

        return imageMetadataMapper.toImageMetadataDto(savedImage);
    }

    @Transactional
    public void deleteImage(String name) {
        final ImageMetadata metadata = imageMetadataRepository.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ServiceException("Unable to find image '%s'", name));

        imageMetadataRepository.delete(metadata);
        s3Service.deleteObject(metadata.getName());
    }

    private void fillImageMetadata(ImageMetadata imageMetadata, MultipartFile file) {
        imageMetadata.setSize(file.getSize());
        imageMetadata.setFileExtension(getFileExtension(file.getOriginalFilename()));
        imageMetadata.setLastUpdated(LocalDateTime.now());
    }

    private String getFileExtension(String fileName) {
         return Optional.of(fileName)
                 .filter(name -> name.contains("."))
                 .map(name -> name.split("\\."))
                 .map(segments -> segments[segments.length - 1])
                 .orElseGet(() -> {
                     log.warn("Unable to retrieve extension for file {}, setting to 'not specified", fileName);
                     return FILE_EXTENSION_NOT_SPECIFIED;
                 });
    }
}
