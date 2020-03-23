package ru.geracimov.otus.java.orm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.Account;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleJdbcAccountServiceTest extends AbstractTest {
    private Account account1;
    private Account account2;
    private Account account2c;
    private Account account3;
    private Account account4;

    @BeforeEach
    public void setUpData() {
        account1 = new Account(1L, "1account1", BigDecimal.valueOf(1));
        account2 = new Account(2L, "1account2", BigDecimal.valueOf(2));
        account2c = new Account(9L, "updated", BigDecimal.valueOf(2));
        account3 = new Account(3L, "1account3", BigDecimal.valueOf(3));
        account4 = new Account(4L, "1account4", BigDecimal.valueOf(4));
    }

    @Test
    public void createTest() {
        assertThat(accountService.getById(1)).isEmpty();
        accountService.save(account1);
        assertThat(accountService.getById(1)).get().isEqualTo(account1);
    }

    @Test
    public void updateTest() {
        assertThat(accountService.getById(2)).isEmpty();
        accountService.update(account2);
        assertThat(accountService.getById(2)).isEmpty();
        accountService.save(account2);
        assertThat(accountService.getById(2)).get().isEqualTo(account2);
        account2.setType("updated");
        accountService.update(account2);
        assertThat(accountService.getById(2)).get().isEqualToIgnoringGivenFields(account2c, "no", "rest");
    }

    @Test
    public void createOrUpdateTest() {
        assertThat(accountService.getById(3)).isEmpty();
        accountService.createOrUpdate(account3);
        assertThat(accountService.getById(3)).get().isEqualTo(account3);
    }

    @Test
    public void loadTest() {
        assertThat(accountService.getById(4)).isEmpty();
        accountService.save(account4);
        assertThat(accountService.getById(4)).get().isEqualTo(account4);
    }

}