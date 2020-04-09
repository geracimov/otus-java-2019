package ru.geracimov.otus.java.db.core.dao;

import ru.geracimov.otus.java.db.core.model.User;
import ru.geracimov.otus.java.db.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
  Optional<User> findById(long id);

  long saveUser(User user);

  SessionManager getSessionManager();
}
