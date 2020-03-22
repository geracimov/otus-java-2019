package ru.geracimov.otus.java.orm;

import lombok.SneakyThrows;
import org.h2.jdbc.JdbcSQLNonTransientException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.Account;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.orm.utils.TableCreator;
import ru.geracimov.otus.java.serializer.service.*;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SimpleJdbcTemplateAccountTest {
    private static JdbcTemplate<Account> templateAccount;

    private Account account1;
    private Account account2;
    private Account account3;
    private Account account4;


    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        DataSource dataSource = new DataSourceH2();
        DbExecutor<Account> accountDbExecutor = new DbExecutor<>();
        SerializerServiceFacade facade = new SerializerServiceFacadeImpl(new SelectSerializerService(),
                new InsertSerializerService(),
                new UpdateSerializerService(),
                new MergeSerializerService());
        templateAccount = new SimpleJdbcTemplate<>(accountDbExecutor,
                dataSource.getConnection(),
                facade);
        TableCreator.createAccountTable(dataSource);
        TableCreator.createAccountTable(dataSource);
    }

    @BeforeEach
    public void setUp() {
        account1 = new Account(1L, "1account1", BigDecimal.valueOf(1));
        account2 = new Account(2L, "1account2", BigDecimal.valueOf(2));
        account3 = new Account(3L, "1account3", BigDecimal.valueOf(3));
        account4 = new Account(4L, "1account4", BigDecimal.valueOf(4));
    }

    @Test
    public void createTest() {
        assertThatThrownBy(() -> templateAccount.load(1, Account.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("Account with @Id 1 not found");
        templateAccount.create(account1);
        assertThat(templateAccount.load(1, Account.class)).isEqualTo(account1);
    }

    @Test
    public void updateTest() {
        assertThatThrownBy(() -> templateAccount.load(2, Account.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("Account with @Id 2 not found");
        assertThatThrownBy(() -> templateAccount.update(account2))
                .isInstanceOf(JdbcSQLNonTransientException.class)
                .hasMessageContaining("No data is available [2000-200]");
        templateAccount.create(account2);
        assertThat(templateAccount.load(2, Account.class)).isEqualTo(account2);
    }

    @Test
    public void createOrUpdateTest() {
        assertThatThrownBy(() -> templateAccount.load(3, Account.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("Account with @Id 3 not found");
        templateAccount.createOrUpdate(account3);
        assertThat(templateAccount.load(3, Account.class)).isEqualTo(account3);
    }

    @Test
    public void loadTest() {
        assertThatThrownBy(() -> templateAccount.load(4, Account.class))
                .isInstanceOf(OrmException.class)
                .hasMessageContaining("Account with @Id 4 not found");
        templateAccount.create(account4);
        assertThat(templateAccount.load(4, Account.class)).isEqualTo(account4);
    }

}