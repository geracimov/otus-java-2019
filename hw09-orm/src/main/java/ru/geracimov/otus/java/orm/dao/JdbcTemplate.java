package ru.geracimov.otus.java.orm.dao;

import java.util.Optional;

public interface JdbcTemplate<T> {

    long create(T objectData);

    void update(T objectData);

    void createOrUpdate(T objectData);

    Optional<T> load(long id, Class<T> clazz);

}
