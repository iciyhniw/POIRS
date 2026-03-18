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
@Table(name = "contents")
@Data @AllArgsConstructor @NoArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Заголовок обов'язковий")
    @Size(min = 5, message = "Заголовок занадто короткий")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Текст не може бути порожнім")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @NotNull(message = "Автор має бути вказаний")
    @Column(nullable = false)
    private Long authorId;

    @NotNull(message = "Категорія має бути вказана")
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String status; // PENDING, PUBLISHED, BLOCKED

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
        if (averageRating == null) averageRating = 0.0;
    }
}