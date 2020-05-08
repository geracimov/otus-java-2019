package ru.geracimov.otus.java.ms.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

}
