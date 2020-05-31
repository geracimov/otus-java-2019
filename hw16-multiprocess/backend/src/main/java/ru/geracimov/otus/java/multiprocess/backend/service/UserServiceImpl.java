package ru.geracimov.otus.java.multiprocess.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.geracimov.otus.java.multiprocess.backend.model.User;
import ru.geracimov.otus.java.multiprocess.backend.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

}
