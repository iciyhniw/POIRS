package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Comment;
import com.mediaplatform.media_service.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@Valid @RequestBody Comment comment) {
        return service.create(comment);
    }

    @GetMapping("/comments")
    public List<Comment> getAll() {
        return service.getAll();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Коментар з ID " + id + " не знайдено"));
    }

    @PutMapping("/comments/{id}")
    public Comment update(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        return service.update(id, comment);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/contents/{contentId}/comments")
    public List<Comment> getByContent(@PathVariable Long contentId) {
        return service.getByContentId(contentId);
    }
}