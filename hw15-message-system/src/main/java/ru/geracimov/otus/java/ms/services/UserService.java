package ru.geracimov.otus.java.ms.services;

import ru.geracimov.otus.java.ms.model.User;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface UserService {

    List<User> findAll();

    long saveUser(User user);

}
