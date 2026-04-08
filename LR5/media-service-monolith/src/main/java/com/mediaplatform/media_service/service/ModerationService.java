package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Moderation;
import com.mediaplatform.media_service.repository.ModerationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModerationService {

    private final ModerationRepository repository;

    public ModerationService(ModerationRepository repository) {
        this.repository = repository;
    }

    public List<Moderation> getAll() {
        return repository.findAll();
    }

    public Optional<Moderation> getById(Long id) {
        return repository.findById(id);
    }

    public Moderation create(Moderation moderation) {
        moderation.setId(null);
        return repository.save(moderation);
    }

    public Moderation requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Модерація з ID " + id + " не знайдена"));
    }
}