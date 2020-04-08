package ru.otus.jdbc.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class MetaDataSourceFieldsTest {
    private MetaDataSource metaData;

    @BeforeEach
    void setUp() {
        metaData = new MetaDataSource();
    }

    @Test
    void getTableName() {
        assertThat(metaData.getTableName(Account.class)).isEqualTo("Account");
        assertThat(metaData.getTableName(User.class)).isEqualTo("User");
        assertThat(metaData.getTableName(Account.class)).isEqualTo("Account");
    }


    @Test
    void getFieldNamesExceptId() {
        assertThat(metaData.getFieldNamesExceptId(Account.class))
                .isEqualTo(new String[]{Account.Fields.type, Account.Fields.rest});
        assertThat(metaData.getFieldNamesExceptId(User.class))
                .isEqualTo(new String[]{User.Fields.name, User.Fields.age});
    }

    @Test
    void getFieldNames() {
        assertThat(metaData.getFieldNames(Account.class))
                .isEqualTo(new String[]{Account.Fields.no, Account.Fields.type, Account.Fields.rest});
        assertThat(metaData.getFieldNames(User.class))
                .isEqualTo(new String[]{User.Fields.id, User.Fields.name, User.Fields.age});
    }

    @Test
    void getFieldIdName() {
        assertThat(metaData.getFieldIdName(Account.class)).isEqualTo("no");
        assertThat(metaData.getFieldIdName(User.class)).isEqualTo("id");
    }
}