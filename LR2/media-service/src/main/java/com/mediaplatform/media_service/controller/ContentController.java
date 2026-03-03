package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.service.ContentService;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contents")
public class ContentController {
    private final ContentService service;

    public ContentController(ContentService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Коректний код 201
    public Content create(@Valid @RequestBody Content content) { // Анотація @Valid активована
        return service.createContent(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok) // Код 200 OK
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + id + " не знайдено")); // Викидає 404 через Handler
    }

    @GetMapping
    public List<Content> getAll() {
        return service.getAllContents();
    }
}