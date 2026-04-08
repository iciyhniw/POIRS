package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.integration.ContentServiceClient;
import com.mediaplatform.media_service.model.Comment;
import com.mediaplatform.media_service.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository repository;
    private final ContentServiceClient contentServiceClient;

    public CommentService(CommentRepository repository, ContentServiceClient contentServiceClient) {
        this.repository = repository;
        this.contentServiceClient = contentServiceClient;
    }

    public List<Comment> getAll() {
        return repository.findAll();
    }

    public List<Comment> getByContentId(Long contentId) {
        return repository.findByContentId(contentId);
    }

    public Optional<Comment> getById(Long id) {
        return repository.findById(id);
    }

    public Comment create(Comment comment) {
        contentServiceClient.ensureContentExists(comment.getContentId());
        comment.setId(null);
        return repository.save(comment);
    }

    public Comment update(Long id, Comment comment) {
        Comment existing = requireById(id);
        contentServiceClient.ensureContentExists(comment.getContentId());

        comment.setId(id);
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(existing.getCreatedAt());
        }

        return repository.save(comment);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id " + id + " was not found");
        }
        repository.deleteById(id);
    }

    public Comment requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " was not found"));
    }
}
