package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Content;
import org.springframework.stereotype.Repository;

// Приклад специфічного репозиторію для Content
@Repository
public class ContentRepository extends GenericRepository<Content> {
    // Тут можна додати методи фільтрації за категоріями
}