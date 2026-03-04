package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.ContentEditRequest;
import com.mediaplatform.media_service.dto.ModerationDecisionRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.model.ContentEdit;
import com.mediaplatform.media_service.service.ContentService;
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
    @ResponseStatus(HttpStatus.CREATED) // 201
    public Content create(@Valid @RequestBody Content content) {
        return service.createContent(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok) // 200
                .orElseThrow(() -> new EntityNotFoundException("Контент з ID " + id + " не знайдено"));
    }

    @GetMapping
    public List<Content> getAll(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        if (categoryId != null) {
            return service.getContentsByCategoryId(categoryId);
        }
        return service.getAllContents();
    }

    // Бізнес-логіка: публікація
    @PostMapping("/{id}/publish")
    public Content publish(@PathVariable Long id) {
        return service.publish(id);
    }

    // Бізнес-логіка: редагування + історія
    @PostMapping("/{id}/edit")
    public Content edit(@PathVariable Long id, @Valid @RequestBody ContentEditRequest request) {
        return service.edit(id, request);
    }

    @GetMapping("/{id}/history")
    public List<ContentEdit> history(@PathVariable Long id) {
        return service.getHistory(id);
    }

    // Бізнес-логіка: модерація
    @PostMapping("/{id}/moderation")
    public Content moderate(@PathVariable Long id, @Valid @RequestBody ModerationDecisionRequest request) {
        return service.moderate(id, request);
    }
}