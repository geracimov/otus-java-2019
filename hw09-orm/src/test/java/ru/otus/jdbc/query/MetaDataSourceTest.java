package ru.otus.jdbc.query;

import org.junit.jupiter.api.Test;
import ru.otus.AbstractTest;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class MetaDataSourceTest extends AbstractTest {

    @Test
    void selectQuery() {
        assertThat(metaData.getSelectQuery(User.class)).isEqualToIgnoringCase("select id, name, age from user where id = ?");
        assertThat(metaData.getSelectQuery(Account.class))
                .isEqualToIgnoringCase("select no, type, rest from account where no = ?");
    }

    @Test
    void insertQuery() {
        assertThat(metaData.getInsertQuery(User.class)).isEqualToIgnoringCase("insert into user (name, age) values (?, ?)");
        assertThat(metaData.getInsertQuery(Account.class))
                .isEqualToIgnoringCase("insert into account (type, rest) values (?, ?)");
    }

    @Test
    void updateQuery() {
        assertThat(metaData.getUpdateQuery(User.class))
                .isEqualToIgnoringCase("update user set name = ?, age = ? where id = ?");
        assertThat(metaData.getUpdateQuery(Account.class))
                .isEqualToIgnoringCase("update Account set type = ?, rest = ? where no = ?");

    }
}