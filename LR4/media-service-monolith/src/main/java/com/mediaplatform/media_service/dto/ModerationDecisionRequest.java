package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.NotBlank;

public class ModerationDecisionRequest {

    @NotBlank(message = "resolution обов'язковий (APPROVED або REJECTED)")
    private String resolution;

    private String reason;

    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}