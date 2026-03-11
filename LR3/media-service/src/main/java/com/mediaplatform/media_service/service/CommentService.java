package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Comment;
import com.mediaplatform.media_service.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
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
        comment.setId(null);
        return repository.save(comment);
    }

    public Comment update(Long id, Comment comment) {
        Comment existing = requireById(id);

        comment.setId(id);
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(existing.getCreatedAt());
        }

        return repository.save(comment);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Коментар з ID " + id + " не знайдено");
        }
        repository.deleteById(id);
    }

    public Comment requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Коментар з ID " + id + " не знайдено"));
    }
}