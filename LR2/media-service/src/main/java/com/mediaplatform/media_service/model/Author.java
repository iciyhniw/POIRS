package com.mediaplatform.media_service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 2. Автор (User)
@Data @AllArgsConstructor @NoArgsConstructor
public class Author {
    private Long id;

    @NotBlank(message = "username обов'язковий")
    @Size(min = 3, max = 50, message = "username має бути 3..50 символів")
    private String username;

    @NotBlank(message = "email обов'язковий")
    @Email(message = "email має бути валідним")
    private String email;
}