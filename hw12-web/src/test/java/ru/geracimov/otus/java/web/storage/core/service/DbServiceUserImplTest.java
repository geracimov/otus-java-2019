package ru.geracimov.otus.java.web.storage.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;
import ru.geracimov.otus.java.web.storage.core.dao.UserDao;
import ru.geracimov.otus.java.web.storage.redis.dao.UserDaoRedis;
import ru.geracimov.otus.java.web.storage.redis.sessionmanager.SessionManagerRedis;

class DbServiceUserImplTest {


    DBServiceUser dbServiceUser;
    UserDao dao;
    SessionManagerRedis sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManagerRedis(new JedisPool("172.17.0.2"));
        dao = new UserDaoRedis(sessionManager);
        dbServiceUser = new DbServiceUserImpl(dao);
    }

    @Test
    void getUser() {
        System.out.println("dbServiceUser.getUser() = " + dbServiceUser.getUser(6));
    }
}