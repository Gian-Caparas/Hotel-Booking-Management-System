package com.hotel.wildcat_hotel.service;

import java.util.List;
import java.util.Optional;

import com.hotel.wildcat_hotel.core.Repository;
import com.hotel.wildcat_hotel.core.Service;
import com.hotel.wildcat_hotel.core.Validator;

public abstract class AbstractCrudService<T> implements Service<T> {

    private final Repository<T> repository;
    private final Validator<T> validator;

    protected AbstractCrudService(Repository<T> repository, Validator<T> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public T create(T entity) {
        validator.validate(entity);
        return repository.save(entity);
    }

    @Override
    public T update(T entity) {
        validator.validate(entity);
        return repository.update(entity);
    }

    @Override
    public boolean delete(int id) {
        return repository.deleteById(id);
    }
}