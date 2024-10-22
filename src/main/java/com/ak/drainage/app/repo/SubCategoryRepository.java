package com.ak.drainage.app.repo;

import com.ak.drainage.app.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findByName(String name);
}
