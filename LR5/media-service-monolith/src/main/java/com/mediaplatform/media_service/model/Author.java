package com.mediaplatform.media_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authors")
@Data @AllArgsConstructor @NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username обов'язковий")
    @Size(min = 3, max = 50, message = "username має бути 3..50 символів")
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank(message = "email обов'язковий")
    @Email(message = "email має бути валідним")
    @Column(nullable = false)
    private String email;
}