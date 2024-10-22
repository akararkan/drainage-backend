package com.ak.drainage.app.api;
import com.ak.drainage.app.model.Category;
import com.ak.drainage.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/addCategory")
    public ResponseEntity<Category> createCategory(
            @RequestPart("category") Category category,
            @RequestPart("thumbnail") MultipartFile thumbnail) {
        return categoryService.addCategory(category, thumbnail);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") Category category,
            @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail) {
        return categoryService.updateCategory(id, category, thumbnail);
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
