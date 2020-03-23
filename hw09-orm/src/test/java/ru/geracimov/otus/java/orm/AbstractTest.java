package ru.geracimov.otus.java.orm;


import org.junit.jupiter.api.BeforeEach;
import ru.geracimov.otus.java.orm.dao.Dao;
import ru.geracimov.otus.java.orm.dao.impl.AccountDaoJdbc;
import ru.geracimov.otus.java.orm.dao.impl.UserDaoJdbc;
import ru.geracimov.otus.java.orm.example.Account;
import ru.geracimov.otus.java.orm.example.User;
import ru.geracimov.otus.java.orm.service.Service;
import ru.geracimov.otus.java.orm.service.impl.AccountServiceImpl;
import ru.geracimov.otus.java.orm.service.impl.UserServiceImpl;
import ru.geracimov.otus.java.orm.utils.TableCreator;
import ru.geracimov.otus.java.serializer.service.*;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;

public abstract class AbstractTest {
    protected Service<User> userService;
    protected Service<Account> accountService;

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new DataSourceH2();
        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor<User> dbUserExecutor = new DbExecutor<>();
        DbExecutor<Account> dbAccountExecutor = new DbExecutor<>();
        SerializerServiceFacade facade = new SerializerServiceFacadeImpl(
                new SelectSerializerService(),
                new InsertSerializerService(),
                new UpdateSerializerService(),
                new MergeSerializerService()
        );
        Dao<User> userDao = new UserDaoJdbc(sessionManager, dbUserExecutor, facade);
        Dao<Account> accountDao = new AccountDaoJdbc(sessionManager, dbAccountExecutor, facade);

        userService = new UserServiceImpl(userDao);
        accountService = new AccountServiceImpl(accountDao);
        TableCreator.createUserTable(dataSource);
        TableCreator.createAccountTable(dataSource);
    }

}
