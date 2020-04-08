package ru.otus.core.service;

import org.junit.jupiter.api.Test;
import ru.otus.AbstractTest;
import ru.otus.core.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class DBServiceAccountImplTest extends AbstractTest {

    @Test
    public void createAccountTest() {
        final long id = accountService.saveAccount(ACCOUNT0);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    public void getAccountTest() {
        final long id = accountService.saveAccount(ACCOUNT1);
        final Optional<Account> optionalAccount = accountService.getAccount(id);
        assertThat(optionalAccount)
                .isNotEmpty()
                .get()
                .hasFieldOrPropertyWithValue(Account.Fields.rest, ACCOUNT1.getRest())
                .hasFieldOrPropertyWithValue(Account.Fields.type, ACCOUNT1.getType());
    }

    @Test
    public void updateAccountTest() {
        final long no = accountService.saveAccount(ACCOUNT2);
        ACCOUNT2.setNo(no);
        ACCOUNT2.setType("updated");
        final BigDecimal newRest = new BigDecimal(100);
        ACCOUNT2.setRest(newRest);
        final long idAfterUpdate = accountService.updateAccount(ACCOUNT2);

        final Optional<Account> optionalAccount = accountService.getAccount(idAfterUpdate);
        assertThat(optionalAccount).isNotEmpty().get()
                                   .hasFieldOrPropertyWithValue(Account.Fields.type, "updated")
                                   .hasFieldOrPropertyWithValue(Account.Fields.rest, newRest);
    }

}