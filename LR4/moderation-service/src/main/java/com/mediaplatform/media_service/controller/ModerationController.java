package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.ModerationDecisionRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Moderation;
import com.mediaplatform.media_service.service.ModerationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ModerationController {

    private final ModerationService service;

    public ModerationController(ModerationService service) {
        this.service = service;
    }

    @PostMapping("/contents/{contentId}/moderations")
    @ResponseStatus(HttpStatus.CREATED)
    public Moderation moderate(@PathVariable Long contentId,
                               @Valid @RequestBody ModerationDecisionRequest request) {
        return service.moderate(contentId, request);
    }

    @GetMapping("/moderations")
    public List<Moderation> getAll() {
        return service.getAll();
    }

    @GetMapping("/moderations/{id}")
    public ResponseEntity<Moderation> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Moderation with id " + id + " was not found"));
    }
}
