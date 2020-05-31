package ru.geracimov.otus.java.multiprocess.backend.repository;


import ru.geracimov.otus.java.multiprocess.backend.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User saveUser(User user);

}