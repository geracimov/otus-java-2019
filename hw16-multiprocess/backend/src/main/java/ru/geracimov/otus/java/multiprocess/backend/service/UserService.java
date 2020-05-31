package ru.geracimov.otus.java.multiprocess.backend.service;

import ru.geracimov.otus.java.multiprocess.backend.model.User;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface UserService {

    List<User> findAll();

    User saveUser(User user);

}
