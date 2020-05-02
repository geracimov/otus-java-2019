package ru.geracimov.otus.java.web.server.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geracimov.otus.java.web.server.model.User;
import ru.geracimov.otus.java.web.server.repository.UserRepository;

import java.util.List;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long saveUser(User user) {
        return userRepository.saveUser(user);
    }

}
