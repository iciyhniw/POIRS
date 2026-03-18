package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findByCategoryId(Long categoryId, Pageable pageable);
}