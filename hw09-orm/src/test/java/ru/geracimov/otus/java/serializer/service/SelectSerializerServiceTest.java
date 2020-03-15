package ru.geracimov.otus.java.serializer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.Account;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectSerializerServiceTest {

    private VisitorService selectService;
    private Account account2;


    @BeforeEach
    public void beforeAll() {
        selectService = new SelectSerializerService();
        account2 = new Account(0L, "1account2", BigDecimal.ZERO);
    }

    @Test
    public void serialize() {
        String accountSelectSql = "select no,type,rest from Account where no=?";
        assertThat(selectService.serialize(Account.class))
                .isEqualToIgnoringWhitespace(accountSelectSql);
        assertThat(selectService.serialize(account2.getClass()))
                .isEqualToIgnoringWhitespace(accountSelectSql);
    }
}