package ru.geracimov.otus.java.db.core.service;

import ru.geracimov.otus.java.db.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

  long saveUser(User user);

  Optional<User> getUser(long id);

  Optional<User> getUserWithAddress(long id);

}
