package com.mediaplatform.media_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    @Column(nullable = false)
    private Long contentId;

    @NotNull(message = "authorId обов'язковий")
    @Column(nullable = false)
    private Long authorId;

    @NotBlank(message = "text не може бути порожнім")
    @Size(min = 1, max = 500, message = "text має бути 1..500 символів")
    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}