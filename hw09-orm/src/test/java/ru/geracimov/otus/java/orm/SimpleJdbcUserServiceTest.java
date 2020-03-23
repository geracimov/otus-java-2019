package ru.geracimov.otus.java.orm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.User;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleJdbcUserServiceTest extends AbstractTest {
    private User user1;
    private User user2;
    private User user2c;
    private User user3;
    private User user4;

    @BeforeEach
    public void setUpData() {
        user1 = new User(1L, "1user1", 101);
        user2 = new User(2L, "1user2", 102);
        user2c = new User(9L, "updated", 200);
        user3 = new User(3L, "1user3", 103);
        user4 = new User(4L, "1user4", 104);
    }

    @Test
    public void createTest() {
        assertThat(userService.getById(1)).isEmpty();
        userService.save(user1);
        assertThat(userService.getById(1)).get().isEqualTo(user1);
    }

    @Test
    public void updateTest() {
        assertThat(userService.getById(2)).isEmpty();
        userService.update(user2);
        assertThat(userService.getById(2)).isEmpty();
        userService.save(user2);
        assertThat(userService.getById(2)).get().isEqualTo(user2);
        user2.setName("updated");
        user2.setAge(200);
        userService.update(user2);
        assertThat(userService.getById(2)).get().isEqualToIgnoringGivenFields(user2c,"id");

    }

    @Test
    public void createOrUpdateTest() {
        assertThat(userService.getById(3)).isEmpty();
        userService.createOrUpdate(user3);
        assertThat(userService.getById(3)).get().isEqualTo(user3);
    }

    @Test
    public void loadTest() {
        assertThat(userService.getById(4)).isEmpty();
        userService.save(user4);
        assertThat(userService.getById(4)).get().isEqualTo(user4);
    }

}