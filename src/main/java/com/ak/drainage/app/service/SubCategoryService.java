package com.ak.drainage.app.service;

import com.ak.drainage.app.model.Category;
import com.ak.drainage.app.model.SubCategory;
import com.ak.drainage.app.repo.CategoryRepository;
import com.ak.drainage.app.repo.SubCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final S3Client s3Client;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;  // Set via application.properties or application.yml

    public ResponseEntity<SubCategory> addSubCategory(SubCategory subCategory, MultipartFile file, Long categoryId) {
        log.info("Adding SubCategory");

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            log.warn("Category with ID {} not found", categoryId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String thumbnailUrl = uploadThumbnailToS3(file);
        if (thumbnailUrl == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SubCategory newSubCategory = SubCategory.builder()
                .name(subCategory.getName())
                .description(subCategory.getDescription())
                .category(categoryOpt.get())
                .thumbnail(thumbnailUrl)
                .createdAt(new Date())
                .build();

        subCategoryRepository.save(newSubCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubCategory);
    }




    // Helper Method: Upload a thumbnail to S3 and return the URL
    private String uploadThumbnailToS3(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("No file provided for upload");
            return null;
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();  // Remove ACL setting

            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            log.info("Thumbnail uploaded to S3: {}", response);
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsRegion, fileName);

        } catch (IOException e) {
            log.error("Error uploading thumbnail to S3", e);
            return null;
        }
    }

}
