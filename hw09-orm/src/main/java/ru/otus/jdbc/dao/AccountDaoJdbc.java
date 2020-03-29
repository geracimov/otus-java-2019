package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.AccountDaoException;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.query.JdbcSqlMaker;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<Account> dbExecutor;
    private final JdbcSqlMaker<Account> jdbcSqlMaker;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<Account> dbExecutor,
                          JdbcSqlMaker<Account> jdbcSqlMaker) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.jdbcSqlMaker = jdbcSqlMaker;
    }


    @Override
    public Optional<Account> findById(long id) {
        try {
            return dbExecutor.selectRecord(getConnection(),
                                           jdbcSqlMaker.selectQuery(),
                                           id,
                                           resultSet -> {
                                               try {
                                                   if (resultSet.next()) {
                                                       return new Account(resultSet.getLong(Account.Fields.no),
                                                                          resultSet.getString(Account.Fields.type),
                                                                          resultSet.getBigDecimal(Account.Fields.rest));
                                                   }
                                               } catch (SQLException e) {
                                                   logger.error(e.getMessage(), e);
                                               }
                                               return null;
                                           });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    @Override
    public long saveAccount(Account account) {
        try {
            return dbExecutor.insertRecord(getConnection(),
                                           jdbcSqlMaker.insertQuery(),
                                           List.of(account.getType(), String.valueOf(account.getRest())));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AccountDaoException(e);
        }
    }

    @Override
    public long updateAccount(Account account) {
        try {
            return dbExecutor.insertRecord(getConnection(),
                                           jdbcSqlMaker.updateQuery(),
                                           List.of(account.getType(),
                                                   String.valueOf(account.getRest()),
                                                   String.valueOf(account.getNo())));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AccountDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
