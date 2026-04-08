package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.ContentEditRequest;
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

    public ContentController(ContentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Content create(@Valid @RequestBody Content content) {
        return service.createContent(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public List<Content> getAll(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "direction", required = false) String direction
    ) {
        return service.getAllContents(categoryId, page, size, sortBy, direction);
    }

    @PutMapping("/{id}")
    public Content update(@PathVariable Long id, @Valid @RequestBody Content content) {
        return service.update(id, content);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/publish")
    public Content publish(@PathVariable Long id) {
        return service.publish(id);
    }

    @PostMapping("/{id}/edit")
    public Content edit(@PathVariable Long id, @Valid @RequestBody ContentEditRequest request) {
        return service.edit(id, request);
    }

    @GetMapping("/{id}/history")
    public List<ContentEdit> history(@PathVariable Long id) {
        return service.getHistory(id);
    }
}
