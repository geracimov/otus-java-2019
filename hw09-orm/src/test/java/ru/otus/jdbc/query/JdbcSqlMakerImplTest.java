package ru.otus.jdbc.query;

import org.junit.jupiter.api.Test;
import ru.otus.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcSqlMakerImplTest extends AbstractTest {

    @Test
    void selectQuery() {
        assertThat(userJdbcSqlMaker.selectQuery()).isEqualToIgnoringCase("select id, name, age from user where id = ?");
        assertThat(accountJdbcSqlMaker.selectQuery())
                .isEqualToIgnoringCase("select no, type, rest from account where no = ?");
    }

    @Test
    void insertQuery() {
        assertThat(userJdbcSqlMaker.insertQuery()).isEqualToIgnoringCase("insert into user (name, age) values (?, ?)");
        assertThat(accountJdbcSqlMaker.insertQuery())
                .isEqualToIgnoringCase("insert into account (type, rest) values (?, ?)");
    }

    @Test
    void updateQuery() {
        assertThat(userJdbcSqlMaker.updateQuery())
                .isEqualToIgnoringCase("update user set name = ?, age = ? where id = ?");
        assertThat(accountJdbcSqlMaker.updateQuery())
                .isEqualToIgnoringCase("update Account set type = ?, rest = ? where no = ?");

    }
}