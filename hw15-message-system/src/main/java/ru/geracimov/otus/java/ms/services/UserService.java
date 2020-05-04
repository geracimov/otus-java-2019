package ru.geracimov.otus.java.ms.services;

import ru.geracimov.otus.java.ms.model.User;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public interface UserService {

    List<User> findAll();

    long saveUser(User user);

    Optional<User> findById(long id);
}
