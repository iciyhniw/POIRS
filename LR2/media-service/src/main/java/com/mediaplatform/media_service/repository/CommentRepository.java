package com.mediaplatform.media_service.repository;

import com.mediaplatform.media_service.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository extends GenericRepository<Comment> {

    public List<Comment> findByContentId(Long contentId) {
        return storage.values().stream()
                .filter(c -> c != null && contentId != null && contentId.equals(c.getContentId()))
                .toList();
    }
}