package com.hotel.wildcat_hotel.core;

public interface Validator<T> {

    void validate(T entity);
}