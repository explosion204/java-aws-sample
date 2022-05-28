package com.epam.awslearning.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.epam.awslearning.exception.ServiceException;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {
    private static final String NAME_METADATA_KEY = "Name";
    private static final String JPEG_MIME_TYPE = "image/jpg";
    private final AmazonS3 s3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @PostConstruct
    void init() {
        if (!s3.doesBucketExistV2(bucketName)) {
            s3.createBucket(bucketName);
        }
    }

    @SneakyThrows
    public void uploadObject(MultipartFile file, String customName) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata(NAME_METADATA_KEY, file.getOriginalFilename());
        metadata.setContentType(JPEG_MIME_TYPE);

        @Cleanup final InputStream inputStream = file.getInputStream();
        PutObjectRequest request = new PutObjectRequest(bucketName, customName, inputStream, metadata);
        request.setMetadata(metadata);
        s3.putObject(request);
    }

    @SneakyThrows
    public byte[] downloadObject(String objectName) {
        ensureObjectExists(objectName);

        @Cleanup final S3Object object = s3.getObject(bucketName, objectName);
        @Cleanup final S3ObjectInputStream stream = object.getObjectContent();
        return IOUtils.toByteArray(stream);
    }

    public void deleteObject(String objectName) {
        ensureObjectExists(objectName);
        s3.deleteObject(bucketName, objectName);
    }

    private void ensureObjectExists(String objectName) {
        if (!s3.doesObjectExist(bucketName, objectName)) {
            throw new ServiceException("Unable to find object with name '%s'", objectName);
        }
    }
}
