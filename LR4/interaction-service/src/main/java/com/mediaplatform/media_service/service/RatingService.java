package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.dto.RatingCreateRequest;
import com.mediaplatform.media_service.integration.ContentServiceClient;
import com.mediaplatform.media_service.model.Rating;
import com.mediaplatform.media_service.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ContentServiceClient contentServiceClient;

    public RatingService(RatingRepository ratingRepository, ContentServiceClient contentServiceClient) {
        this.ratingRepository = ratingRepository;
        this.contentServiceClient = contentServiceClient;
    }

    @Transactional
    public Rating addRating(Long contentId, RatingCreateRequest request) {
        contentServiceClient.ensureContentExists(contentId);

        Rating rating = new Rating();
        rating.setId(null);
        rating.setContentId(contentId);
        rating.setAuthorId(request.authorId());
        rating.setScore(request.score());

        Rating savedRating = ratingRepository.save(rating);
        Double average = getAverageScore(contentId);
        contentServiceClient.updateAverageRating(contentId, average == null ? 0.0 : average);

        return savedRating;
    }

    public Double getAverageScore(Long contentId) {
        return ratingRepository.findAverageScoreByContentId(contentId);
    }
}
