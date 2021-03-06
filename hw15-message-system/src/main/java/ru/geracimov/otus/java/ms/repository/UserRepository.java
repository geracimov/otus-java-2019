package ru.geracimov.otus.java.ms.repository;

import ru.geracimov.otus.java.ms.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    long saveUser(User user);

}