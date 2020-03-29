package ru.otus.core.service;

import org.junit.jupiter.api.Test;
import ru.otus.AbstractTest;
import ru.otus.core.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class DBServiceUserImplTest extends AbstractTest {

    @Test
    public void createUserTest() {
        final long id = userService.saveUser(USER0);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    public void getUserTest() {
        final long id = userService.saveUser(USER1);
        final Optional<User> optionalUser = userService.getUser(id);
        assertThat(optionalUser)
                .isNotEmpty()
                .get()
                .hasFieldOrPropertyWithValue(User.Fields.name, USER1.getName())
                .hasFieldOrPropertyWithValue(User.Fields.age, USER1.getAge());
    }

    @Test
    public void updateUserTest() {
        final long id = userService.saveUser(USER2);
        USER2.setId(id);
        USER2.setName("updated");
        USER2.setAge(100);
        final long idAfterUpdate = userService.updateUser(USER2);

        final Optional<User> optionalUser = userService.getUser(idAfterUpdate);
        assertThat(optionalUser).isNotEmpty().get()
                                .hasFieldOrPropertyWithValue(User.Fields.name, "updated")
                                .hasFieldOrPropertyWithValue(User.Fields.age, 100);
    }
}