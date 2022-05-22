package com.epam.awslearning.model.repository;

import com.epam.awslearning.model.entity.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {
    List<ImageMetadata> findByName(String name);

    @Query(value = "SELECT * FROM image_metadata ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<ImageMetadata> findRandom();

}
