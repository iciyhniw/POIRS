package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RatingCreateRequest {

    @NotNull(message = "score є обов'язковим")
    @Min(value = 1, message = "score має бути в діапазоні 1..5")
    @Max(value = 5, message = "score має бути в діапазоні 1..5")
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}