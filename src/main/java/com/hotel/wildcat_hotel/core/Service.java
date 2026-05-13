package com.hotel.wildcat_hotel.core;

import java.util.List;
import java.util.Optional;

public interface Service<T> {

    List<T> getAll();

    Optional<T> getById(int id);

    T create(T entity);

    T update(T entity);

    boolean delete(int id);
}