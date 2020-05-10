package ru.geracimov.otus.java.multiprocess.backend.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import ru.geracimov.otus.java.multiprocess.backend.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
public class RedisUserRepository implements UserRepository {
    public static final String KEY = User.class.getName();
    public static final String ID_DE = ":";
    public static final String LOGIN_DE = "#";
    private final ObjectMapper mapper;
    private final JedisPool jedisPool;
    private final Random random;

    public RedisUserRepository(@NonNull JedisPool jedisPool, ObjectMapper objectMapper) {
        this.jedisPool = jedisPool;
        this.random = new Random();
        this.mapper = objectMapper;
        this.mapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public List<User> findAll() {
        @Cleanup Jedis jedis = jedisPool.getResource();
        val keys = jedis.keys(String.format("%s%s*", KEY, ID_DE));
        if (keys == null || keys.size() == 0) return new ArrayList<>();
        val values = jedis.mget(keys.toArray(new String[0]));
        return values.stream().map(s -> {
            try {
                return mapper.readValue(s, User.class);
            } catch (JsonProcessingException e) {
                throw new UserDaoException(e);
            }
        }).sorted(Comparator.comparingLong(User::getId)).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public User saveUser(@NonNull User user) {
        if (user.getId() == 0) {
            user.setId(getId());
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new UserDaoException("Login cannot be null");
        }
        @Cleanup Jedis jedis = jedisPool.getResource();
        if (jedis.exists(prepareLogin(user.getLogin()))) {
            throw new UserDaoException("Login already exists");
        }
        final Transaction multi = jedis.multi();
        multi.set(prepareId(user.getId()), mapper.writeValueAsString(user));
        multi.set(prepareLogin(user.getLogin()), String.valueOf(user.getId()));
        multi.exec();
        return user;
    }

    private long getId() {
        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.incr(KEY);
    }

    private String prepareId(long id) {
        return KEY + ID_DE + id;
    }

    private String prepareLogin(String login) {
        return KEY + LOGIN_DE + login;
    }

}
