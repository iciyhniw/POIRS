package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.RatingCreateRequest;
import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contents/{contentId}/ratings")
public class RatingController {

    private final ContentService contentService;

    public RatingController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Content addRating(@PathVariable Long contentId, @Valid @RequestBody RatingCreateRequest request) {
        return contentService.addRating(contentId, request.getScore());
    }
}