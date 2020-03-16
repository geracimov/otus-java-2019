package ru.geracimov.otus.java.orm;

import lombok.SneakyThrows;
import org.h2.jdbc.JdbcSQLNonTransientException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.User;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.orm.utils.TableCreator;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SimpleJdbcTemplateTest {
    private static JdbcTemplate<User> templateUser;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        DataSource dataSource = new DataSourceH2();
        DbExecutor<User> userDbExecutor = new DbExecutor<>();
        templateUser = new SimpleJdbcTemplate<>(userDbExecutor, dataSource.getConnection());
        TableCreator.createUserTable(dataSource);
        TableCreator.createAccountTable(dataSource);
    }

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "1user1", 101);
        user2 = new User(2L, "1user2", 102);
        user3 = new User(3L, "1user3", 103);
        user4 = new User(4L, "1user4", 104);
    }

    @Test
    public void createTest() {
        assertThatThrownBy(() -> templateUser.load(1, User.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("User with @Id 1 not found");
        templateUser.create(user1);
        assertThat(templateUser.load(1, User.class)).isEqualTo(user1);
    }

    @Test
    public void updateTest() {
        assertThatThrownBy(() -> templateUser.load(2, User.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("User with @Id 2 not found");
        assertThatThrownBy(() -> templateUser.update(user2))
                .isInstanceOf(JdbcSQLNonTransientException.class)
                .hasMessageContaining("No data is available [2000-200]");
        templateUser.create(user2);
        assertThat(templateUser.load(2, User.class)).isEqualTo(user2);
    }

    @Test
    public void createOrUpdateTest() {
        assertThatThrownBy(() -> templateUser.load(3, User.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("User with @Id 3 not found");
        templateUser.createOrUpdate(user3);
        assertThat(templateUser.load(3, User.class)).isEqualTo(user3);
    }

    @Test
    public void loadTest() {
        assertThatThrownBy(() -> templateUser.load(4, User.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("User with @Id 4 not found");
        templateUser.create(user4);
        assertThat(templateUser.load(4, User.class)).isEqualTo(user4);

    }

}