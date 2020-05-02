package ru.geracimov.otus.java.web.server.repository;

import ru.geracimov.otus.java.web.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    long saveUser(User user);

}