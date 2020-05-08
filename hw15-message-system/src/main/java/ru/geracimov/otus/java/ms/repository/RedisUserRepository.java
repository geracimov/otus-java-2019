package ru.geracimov.otus.java.ms.repository;

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
import ru.geracimov.otus.java.ms.model.User;

import java.util.*;
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
    public Optional<User> findById(long id) {
        @Cleanup Jedis jedis = jedisPool.getResource();
        final String user = jedis.get(prepareId(id));
        return Optional.of(mapper.readValue(user, User.class));
    }

    @Override
    @SneakyThrows
    public Optional<User> findRandomUser() {
        @Cleanup Jedis jedis = jedisPool.getResource();
        val keys = jedis.keys(String.format("%s%s*", KEY, ID_DE));
        if (keys == null || keys.size() == 0) return Optional.empty();
        final int index = random.nextInt(keys.size());
        final String key = keys.toArray(new String[0])[index];
        final User user = mapper.readValue(jedis.get(key), User.class);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        if (login == null || login.isEmpty()) return Optional.empty();
        @Cleanup Jedis jedis = jedisPool.getResource();
        final String userId = jedis.get(prepareLogin(login));
        if (userId == null || userId.isEmpty()) return Optional.empty();
        return findById(Long.parseLong(userId));
    }

    @Override
    @SneakyThrows
    public long saveUser(@NonNull User user) {
        if (user.getId() == 0) {
            user.setId(getId());
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()){
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
        return user.getId();
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
