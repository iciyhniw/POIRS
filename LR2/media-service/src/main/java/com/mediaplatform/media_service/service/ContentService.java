package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.model.Content;
import com.mediaplatform.media_service.model.Rating;
import com.mediaplatform.media_service.repository.ContentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {
    private final ContentRepository repository;
    private final List<Rating> ratings = new ArrayList<>(); // In-memory список рейтингів

    public ContentService(ContentRepository repository) {
        this.repository = repository;
    }

    public List<Content> getAllContents() {
        return repository.findAll();
    }

    public Optional<Content> getById(Long id) {
        return repository.findById(id);
    }

    public Content createContent(Content content) {
        content.setId(repository.nextId());
        content.setStatus("PENDING"); // Початковий статус для модерації
        return repository.save(content.getId(), content);
    }

    // Бізнес-логіка: підрахунок середнього рейтингу
    public void addRating(Long contentId, Integer score) {
        repository.findById(contentId).ifPresent(c -> {
            // Логіка оновлення averageRating
            double newAvg = (c.getAverageRating() + score) / 2; // спрощено
            c.setAverageRating(newAvg);
        });
    }
}