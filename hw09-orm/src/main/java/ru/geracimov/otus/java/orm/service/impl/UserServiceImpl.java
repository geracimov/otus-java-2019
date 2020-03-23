package ru.geracimov.otus.java.orm.service.impl;


import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.dao.Dao;
import ru.geracimov.otus.java.orm.example.User;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.orm.service.Service;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements Service<User> {
    private final Dao<User> userDao;

    @Override
    public long save(User user) {
        @Cleanup
        SessionManager sessionManager = userDao.getSessionManager();
        sessionManager.beginSession();
        try {
            long userId = userDao.save(user);
            sessionManager.commitSession();
            log.info("created user: {}", userId);
            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
            throw new OrmException(e);
        }
    }

    @Override
    public Optional<User> getById(long id) {
        @Cleanup
        SessionManager sessionManager = userDao.getSessionManager();
        sessionManager.beginSession();
        try {
            Optional<User> userOptional = userDao.findById(id);
            log.info("GET user: {}", userOptional.orElse(null));
            return userOptional;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) {
        @Cleanup
        SessionManager sessionManager = userDao.getSessionManager();
        sessionManager.beginSession();
        try {
            userDao.update(user);
            sessionManager.commitSession();
            log.info("UPD user: {}", user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
    }

    @Override
    public void createOrUpdate(User user) {
        @Cleanup
        SessionManager sessionManager = userDao.getSessionManager();
        sessionManager.beginSession();
        try {
            userDao.createOrUpdate(user);
            sessionManager.commitSession();
            log.info("UPD user: {}", user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
    }

}
