package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.ContentEdit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentEditRepository extends GenericRepository<ContentEdit> {

    public List<ContentEdit> findByContentId(Long contentId) {
        return storage.values().stream()
                .filter(e -> e != null && contentId != null && contentId.equals(e.getContentId()))
                .toList();
    }
}