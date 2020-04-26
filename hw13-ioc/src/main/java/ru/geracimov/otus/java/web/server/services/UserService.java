package ru.geracimov.otus.java.web.server.services;

import ru.geracimov.otus.java.web.server.model.User;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface UserService {

    List<User> findAll();

    long saveUser(User user);

}
