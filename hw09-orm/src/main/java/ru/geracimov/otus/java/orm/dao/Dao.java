package ru.geracimov.otus.java.orm.dao;


import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> findById(long id);

    long save(T object);

    void update(T object);

    void createOrUpdate(T object);

    SessionManager getSessionManager();

}
