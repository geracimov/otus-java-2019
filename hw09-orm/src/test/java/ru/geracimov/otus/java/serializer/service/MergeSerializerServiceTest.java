package ru.geracimov.otus.java.serializer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.orm.example.Account;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MergeSerializerServiceTest {


    private VisitorService mergeService;
    private Account account1;
    private Account account2;


    @BeforeEach
    public void beforeAll() {
        mergeService = new MergeSerializerService();
        account1 = new Account(1L, "1account1", BigDecimal.ONE);
        account2 = new Account(2L, "1account2", BigDecimal.valueOf(2));
    }

    @Test
    public void mergeSqlCreateTest() {
        String accountMergeSql = "merge into Account key (no) values (?,?,?)";
        assertThat(mergeService.serialize(account1))
                .isEqualToIgnoringWhitespace(accountMergeSql);
        assertThat(mergeService.serialize(account2))
                .isEqualToIgnoringWhitespace(accountMergeSql);
    }


}