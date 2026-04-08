package com.mediaplatform.media_service.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LR2 legacy in-memory repository.
 * LR3 uses Spring Data JPA repositories instead.
 */
public class GenericRepository<T> {
    protected final Map<Long, T> storage = new ConcurrentHashMap<>();
    protected final AtomicLong idGenerator = new AtomicLong(1);

    public List<T> findAll() { return new ArrayList<>(storage.values()); }

    public Optional<T> findById(Long id) { return Optional.ofNullable(storage.get(id)); }

    public T save(Long id, T entity) {
        storage.put(id, entity);
        return entity;
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public Long nextId() { return idGenerator.getAndIncrement(); }
}