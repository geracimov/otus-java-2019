package ru.geracimov.otus.java.web.server.services;


import ru.geracimov.otus.java.web.server.repository.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByLogin(login)
                      .map(user -> user.getPassword().equals(password))
                      .orElse(false);
    }

}
