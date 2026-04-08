package com.mediaplatform.media_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContentEditRequest {

    @NotBlank(message = "title обов'язковий")
    @Size(min = 5, message = "title занадто короткий")
    private String title;

    @NotBlank(message = "body обов'язковий")
    private String body;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}