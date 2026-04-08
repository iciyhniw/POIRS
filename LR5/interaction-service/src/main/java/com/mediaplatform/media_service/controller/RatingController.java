package com.mediaplatform.media_service.controller;

import com.mediaplatform.media_service.dto.RatingCreateRequest;
import com.mediaplatform.media_service.model.Rating;
import com.mediaplatform.media_service.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contents/{contentId}/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating addRating(@PathVariable Long contentId, @Valid @RequestBody RatingCreateRequest request) {
        return ratingService.addRating(contentId, request);
    }

    @GetMapping("/average")
    public Double getAverage(@PathVariable Long contentId) {
        return ratingService.getAverageScore(contentId);
    }
}
