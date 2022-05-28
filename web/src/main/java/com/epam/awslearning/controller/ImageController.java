package com.epam.awslearning.controller;

import com.epam.awslearning.dto.ImageMetadataDto;
import com.epam.awslearning.dto.ImageUploadDto;
import com.epam.awslearning.service.ImageService;
import com.epam.awslearning.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<ImageMetadataDto>> getAllMetadata() {
        final List<ImageMetadataDto> allMetadata = imageService.findAllMetadata();
        return ResponseEntity.ok(allMetadata);
    }

    @GetMapping(value = "/download/{name}", produces = "application/octet-stream")
    public ResponseEntity<Resource> download(@PathVariable String name) {
        final byte[] data = imageService.downloadImage(name);
        final ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentLength(data.length)
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<ImageMetadataDto> uploadImage(@ModelAttribute ImageUploadDto imageUploadDto) {
        final ImageMetadataDto metadata = imageService.uploadImage(imageUploadDto);
        final String notificationMessage = buildMessage(metadata);
        notificationService.enqueueMessage(notificationMessage);

        return new ResponseEntity<>(metadata, CREATED);
    }

    @GetMapping("/random")
    public ResponseEntity<ImageMetadataDto> getRandomMetadata() {
        final ImageMetadataDto metadata = imageService.findRandomMetadata();
        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        imageService.deleteImage(name);
        return ResponseEntity.noContent().build();
    }

    private String buildMessage(ImageMetadataDto imageMetadata) {
        final WebMvcLinkBuilder downloadLink = linkTo(methodOn(ImageController.class)
                .download(imageMetadata.getName()));

        return String.format(
                "Image uploaded! Name: '%s'.'%s', size: '%s'. Link: %s",
                imageMetadata.getName(),
                imageMetadata.getFileExtension(),
                imageMetadata.getSize(),
                downloadLink
        );
    }
}
