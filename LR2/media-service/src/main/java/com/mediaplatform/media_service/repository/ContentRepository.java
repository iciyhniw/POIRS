package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Content;
import org.springframework.stereotype.Repository;

import java.util.List;

// Приклад специфічного репозиторію для Content
@Repository
public class ContentRepository extends GenericRepository<Content> {

    public List<Content> findByCategoryId(Long categoryId) {
        return storage.values().stream()
                .filter(c -> c != null && categoryId != null && categoryId.equals(c.getCategoryId()))
                .toList();
    }
}