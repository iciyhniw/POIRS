package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 3. Категорія
@Data @AllArgsConstructor @NoArgsConstructor
public class Category {
    private Long id;

    @NotBlank(message = "name обов'язкове")
    @Size(min = 2, max = 50, message = "name має бути 2..50 символів")
    private String name;
}