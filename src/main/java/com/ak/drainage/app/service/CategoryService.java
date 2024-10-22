package com.ak.drainage.app.service;

import com.ak.drainage.app.model.Category;
import com.ak.drainage.app.repo.CategoryRepository;
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
@RequiredArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;  // Set via application.properties or application.yml

    // CREATE: Add a new category with a thumbnail
    public ResponseEntity<Category> addCategory(Category category, MultipartFile thumbnailFile) {
        log.info("Adding category: {}", category);

        String thumbnailUrl = uploadThumbnailToS3(thumbnailFile);
        if (thumbnailUrl == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Category newCategory = Category.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .thumbnail(thumbnailUrl)
                .createdAt(new Date())
                .updatedAt(null)
                .tag(category.getTag())
                .build();
        categoryRepository.save(newCategory);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // READ: Get a category by ID
    public ResponseEntity<Category> getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        } else {
            log.warn("Category not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE: Update an existing category, optionally with a new thumbnail
    public ResponseEntity<Category> updateCategory(Long id, Category updatedCategory, MultipartFile thumbnailFile) {
        log.info("Updating category with ID: {}", id);
        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);

        if (existingCategoryOpt.isPresent()) {
            Category existingCategory = existingCategoryOpt.get();

            String thumbnailUrl = existingCategory.getThumbnail(); // Keep existing thumbnail
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                thumbnailUrl = uploadThumbnailToS3(thumbnailFile);
                if (thumbnailUrl == null) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            existingCategory.setName(updatedCategory.getName());
            existingCategory.setDescription(updatedCategory.getDescription());
            existingCategory.setThumbnail(thumbnailUrl);
            existingCategory.setUpdatedAt(new Date());
            existingCategory.setTag(updatedCategory.getTag());

            categoryRepository.save(existingCategory);
            return new ResponseEntity<>(existingCategory, HttpStatus.OK);
        } else {
            log.warn("Category not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE: Delete a category by ID
    public ResponseEntity<HttpStatus> deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            categoryRepository.delete(categoryOpt.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("Category not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
