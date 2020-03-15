package ru.geracimov.otus.java.serializer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.Account;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class InsertSerializerServiceTest {

    private VisitorService insertService;
    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    public void beforeAll() {
        insertService = new InsertSerializerService();
        account1 = new Account(1L, "1account1", BigDecimal.ONE);
        account2 = new Account(2L, "1account2", BigDecimal.valueOf(2));
        account3 = new Account(3L, "1account3", BigDecimal.valueOf(3));
    }

    @Test
    public void serialize() {
        final String expected = "insert into Account (no,type,rest) values (?,?,?)";
        assertThat(insertService.serialize(account1)).isEqualToIgnoringWhitespace(expected);
        assertThat(insertService.serialize(account2)).isEqualToIgnoringWhitespace(expected);
        assertThat(insertService.serialize(account3)).isEqualToIgnoringWhitespace(expected);
    }
}