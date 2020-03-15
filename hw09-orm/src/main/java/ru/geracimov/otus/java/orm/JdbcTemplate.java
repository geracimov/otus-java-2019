package ru.geracimov.otus.java.orm;

public interface JdbcTemplate<T> {

    void create(T objectData);

    void update(T objectData);

    void createOrUpdate(T objectData);

    T load(long id, Class<T> clazz);

}
