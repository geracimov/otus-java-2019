package ru.geracimov.otus.java.web.server.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.web.server.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void deserializeUserTest() {
        String object = "{\"id\":99,\"name\":\"username\",\"login\":\"login111\",\"password\":\"password111\"}";
        assertThat(mapper.readValue(object, User.class))
                .isInstanceOf(User.class)
                .hasFieldOrPropertyWithValue("id", 99L)
                .hasFieldOrPropertyWithValue("name", "username")
                .hasFieldOrPropertyWithValue("login", "login111")
                .hasFieldOrPropertyWithValue("password", "password111")
        ;
    }
}