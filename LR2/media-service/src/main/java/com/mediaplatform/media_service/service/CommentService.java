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
        comment.setId(repository.nextId());
        return repository.save(comment.getId(), comment);
    }

    public Comment requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Коментар з ID " + id + " не знайдено"));
    }
}