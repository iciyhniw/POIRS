package com.mediaplatform.media_service.service;

import com.mediaplatform.media_service.exception.EntityNotFoundException;
import com.mediaplatform.media_service.model.Author;
import com.mediaplatform.media_service.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> getAll() {
        return repository.findAll();
    }

    public Optional<Author> getById(Long id) {
        return repository.findById(id);
    }

    public Author create(Author author) {
        author.setId(repository.nextId());
        return repository.save(author.getId(), author);
    }

    public Author requireById(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException("Автор з ID " + id + " не знайдено"));
    }
}