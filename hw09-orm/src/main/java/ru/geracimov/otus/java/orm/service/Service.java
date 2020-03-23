package ru.geracimov.otus.java.orm.service;

import java.util.Optional;

public interface Service<T> {

    long save(T object);

    Optional<T> getById(long id);

    void update(T object);

    void createOrUpdate(T object);

}
