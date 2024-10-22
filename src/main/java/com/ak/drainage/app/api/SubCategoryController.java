package com.ak.drainage.app.api;

import com.ak.drainage.app.model.SubCategory;
import com.ak.drainage.app.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/subCategory")
@RequiredArgsConstructor
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping("/addSubCategory")
    public ResponseEntity<SubCategory> createSubCategory(
            @RequestPart("subcategory") SubCategory subCategory,
            @RequestPart("thumbnail") MultipartFile file,
            @RequestParam Long categoryId) {

        return subCategoryService.addSubCategory(subCategory, file, categoryId);
    }

}
