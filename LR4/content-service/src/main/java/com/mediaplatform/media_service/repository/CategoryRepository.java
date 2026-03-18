package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}