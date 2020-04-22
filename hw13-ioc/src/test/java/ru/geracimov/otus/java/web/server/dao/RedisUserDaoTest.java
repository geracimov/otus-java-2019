package ru.geracimov.otus.java.web.server.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;
import ru.geracimov.otus.java.web.server.dao.RedisUserDao;
import ru.geracimov.otus.java.web.server.dao.UserDao;
import ru.geracimov.otus.java.web.server.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RedisUserDaoTest {
    private static RedisServer redisServer;
    private UserDao dao;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void afterAll() {
        redisServer.stop();
    }

    @BeforeEach
    public void setUp() {
        final JedisPool pool = new JedisPool();
        final ObjectMapper mapper = new ObjectMapper();
        dao = new RedisUserDao(pool, mapper);
    }

    @Test
    public void saveNewUserTest() {
        final long id = dao.saveUser(new User(0, "newUser", "newLogin", "newPassword"));
        assertThat(id).isGreaterThan(0);
    }

    @Test
    public void findUserByLoginTest() {
        dao.saveUser(new User(999, "username999", "login999", "password999"));
        final Optional<User> login999 = dao.findByLogin("login999");
        assertThat(login999).isPresent().get()
                            .hasFieldOrPropertyWithValue("id", 999L)
                            .hasFieldOrPropertyWithValue("name", "username999")
                            .hasFieldOrPropertyWithValue("login", "login999")
                            .hasFieldOrPropertyWithValue("password", "password999");
    }

    @Test
    public void findUserByUnknownLoginTest() {
        final Optional<User> unknown = dao.findByLogin("UNKNOWN");
        assertThat(unknown).isNotPresent();
    }

    @Test
    public void findAllTest() {
        final User user = new User(9, "username9", "login9", "password9");
        dao.saveUser(user);
        final List<User> allUsers = dao.findAll();
        assertThat(allUsers).isNotNull().hasSizeGreaterThan(0).contains(user);
    }

    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    public void findRandomUserTest(Integer i) {
        final Optional<User> randomUser = dao.findRandomUser();
        final List<User> all = dao.findAll();

        assertThat(randomUser).isPresent().get().isInstanceOf(User.class).isIn(all);
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return IntStream.range(1, 10).mapToObj(Arguments::of);
    }

}