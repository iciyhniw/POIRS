package com.mediaplatform.media_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Data @AllArgsConstructor @NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "contentId обов'язковий")
    @Column(nullable = false)
    private Long contentId;

    @NotNull(message = "authorId обов'язковий")
    @Column(nullable = false)
    private Long authorId;

    @NotNull(message = "score обов'язковий")
    @Min(value = 1, message = "score має бути 1..5")
    @Max(value = 5, message = "score має бути 1..5")
    @Column(nullable = false)
    private Integer score; // 1-5
}