package com.mediaplatform.media_service.integration;

import com.mediaplatform.media_service.dto.ContentExistsResponse;
import com.mediaplatform.media_service.dto.UpdateContentRatingRequest;
import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ContentServiceClient {

    private final RestClient restClient;

    public ContentServiceClient(@Value("${content.service.base-url}") String contentServiceBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(contentServiceBaseUrl)
                .build();
    }

    public void ensureContentExists(Long contentId) {
        try {
            ContentExistsResponse response = restClient.get()
                    .uri("/api/v1/internal/contents/{id}/exists", contentId)
                    .retrieve()
                    .body(ContentExistsResponse.class);

            if (response == null || !response.exists()) {
                throw new EntityNotFoundException("Content with id " + contentId + " was not found");
            }
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Content service is unavailable", ex);
        }
    }

    public void updateAverageRating(Long contentId, Double averageRating) {
        try {
            restClient.patch()
                    .uri("/api/v1/internal/contents/{id}/rating", contentId)
                    .body(new UpdateContentRatingRequest(averageRating))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Content service is unavailable", ex);
        }
    }
}
