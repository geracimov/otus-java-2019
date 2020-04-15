package ru.geracimov.otus.java.web.storage.redis.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NonNull;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import ru.geracimov.otus.java.web.storage.core.dao.UserDao;
import ru.geracimov.otus.java.web.storage.core.model.User;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.SessionManager;
import ru.geracimov.otus.java.web.storage.redis.sessionmanager.SessionManagerRedis;

import java.util.Optional;

public class UserDaoRedis implements UserDao {
    public static final String KEY = User.class.getName();
    private final SessionManagerRedis sessionManager;
    private final ObjectMapper mapper;

    public UserDaoRedis(@NonNull SessionManagerRedis sessionManager) {
        this.sessionManager = sessionManager;
        this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(long id) {
        final String value = getJedis().get(prepareId(id));
        return value != null
                ? Optional.of(mapper.readValue(value, User.class))
                : Optional.empty();
    }

    @Override
    @SneakyThrows
    public long saveUser(@NonNull User user) {
        if (user.getId() == 0) {
            user.setId(getId());
        }
        getJedis().set(prepareId(user.getId()), mapper.writeValueAsString(user));
        return user.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private long getId() {
        return getJedis().incr(KEY);
    }

    private String prepareId(long id) {
        return KEY + ":" + id;
    }

    private Jedis getJedis() {
        return sessionManager.getCurrentSession().getJedis();
    }

}
