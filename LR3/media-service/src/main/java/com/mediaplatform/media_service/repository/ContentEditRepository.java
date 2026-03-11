package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.ContentEdit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentEditRepository extends JpaRepository<ContentEdit, Long> {
    List<ContentEdit> findByContentId(Long contentId);
}