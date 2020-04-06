package ru.otus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceAccount;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.h2.TableCreator;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.dao.AccountDaoJdbc;
import ru.otus.jdbc.dao.JdbcMapper;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.jdbc.query.MetaDataSource;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;

public abstract class AbstractTest {
    private final static DataSource DATASOURCE = new DataSourceH2();
    protected final static Account ACCOUNT0 = new Account(0L, "0account0", BigDecimal.valueOf(0));
    protected final static Account ACCOUNT1 = new Account(1L, "1account1", BigDecimal.valueOf(1));
    protected final static Account ACCOUNT2 = new Account(2L, "2account2", BigDecimal.valueOf(2));
    protected final static User USER0 = new User(0L, "0user0", 100);
    protected final static User USER1 = new User(1L, "1user1", 101);
    protected final static User USER2 = new User(2L, "2user2", 102);

    protected static MetaDataSource metaData;
    protected static JdbcMapper jdbcMapper;
    protected DBServiceUser userService;
    protected DBServiceAccount accountService;

    @BeforeAll
    static void beforeAll() {
        metaData = new MetaDataSource();
        jdbcMapper = new JdbcMapper(metaData);
        TableCreator.createUserTable(DATASOURCE);
        TableCreator.createAccountTable(DATASOURCE);
    }

    @BeforeEach
    public void setUp() {
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(DATASOURCE);
        DbExecutor<User> dbUserExecutor = new DbExecutor<>();
        DbExecutor<Account> dbAccountExecutor = new DbExecutor<>();
        UserDao userDao = new UserDaoJdbc(sessionManager, dbUserExecutor, metaData, jdbcMapper);
        AccountDao accountDao = new AccountDaoJdbc(sessionManager, dbAccountExecutor, metaData, jdbcMapper);

        userService = new DbServiceUserImpl(userDao);
        accountService = new DbServiceAccountImpl(accountDao);
    }

}
