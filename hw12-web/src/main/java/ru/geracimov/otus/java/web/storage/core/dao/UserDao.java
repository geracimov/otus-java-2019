package ru.geracimov.otus.java.web.storage.core.dao;


import ru.geracimov.otus.java.web.storage.core.model.User;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long saveUser(User user);

    SessionManager getSessionManager();
}
