package ru.geracimov.otus.java.multiprocess.backend.repository;


import ru.geracimov.otus.java.multiprocess.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll( );

    User saveUser( User user );

}