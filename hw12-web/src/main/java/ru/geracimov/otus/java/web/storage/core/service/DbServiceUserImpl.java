package ru.geracimov.otus.java.web.storage.core.service;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ru.geracimov.otus.java.web.storage.core.dao.UserDao;
import ru.geracimov.otus.java.web.storage.core.model.User;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.SessionManager;
import ru.geracimov.otus.java.web.storage.redis.dao.UserDaoRedis;
import ru.geracimov.otus.java.web.storage.redis.sessionmanager.SessionManagerRedis;

import java.util.Optional;

@Slf4j
public class DbServiceUserImpl implements DBServiceUser {

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("172.17.0.2");
        SessionManagerRedis sm = new SessionManagerRedis(jedisPool);
        UserDao dao = new UserDaoRedis(sm);
        DBServiceUser serviceUser = new DbServiceUserImpl(dao);
        final Optional<User> user = serviceUser.getUser(2);
        System.out.println("user = " + user);
    }


    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDao.saveUser(user);
                sessionManager.commitSession();

                log.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                log.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserWithAddress(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                User user = userOptional.orElse(null);
                log.info("user: {}", user);
                return userOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
