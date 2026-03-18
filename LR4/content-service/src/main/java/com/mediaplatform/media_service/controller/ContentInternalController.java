package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.ContentExistsResponse;
import com.mediaplatform.media_service.dto.UpdateContentRatingRequest;
import com.mediaplatform.media_service.dto.UpdateContentStatusRequest;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/contents")
public class ContentInternalController {

    private final ContentService service;

    public ContentInternalController(ContentService service) {
        this.service = service;
    }

    @GetMapping("/{id}/exists")
    public ContentExistsResponse exists(@PathVariable Long id) {
        return new ContentExistsResponse(service.exists(id));
    }

    @PatchMapping("/{id}/rating")
    public Content updateRating(@PathVariable Long id,
                                @Valid @RequestBody UpdateContentRatingRequest request) {
        return service.updateAverageRating(id, request.averageRating());
    }

    @PatchMapping("/{id}/status")
    public Content updateStatus(@PathVariable Long id,
                                @Valid @RequestBody UpdateContentStatusRequest request) {
        return service.updateStatus(id, request.status());
    }
}
