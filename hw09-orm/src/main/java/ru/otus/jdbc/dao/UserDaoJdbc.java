package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDaoException;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.query.JdbcSqlMaker;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
  private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<User> dbExecutor;
  private final JdbcSqlMaker<User> jdbcSqlMaker;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor,  JdbcSqlMaker<User> jdbcSqlMaker) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.jdbcSqlMaker = jdbcSqlMaker;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      return dbExecutor.selectRecord(getConnection(), jdbcSqlMaker.selectQuery(), id, resultSet -> {
        try {
          if (resultSet.next()) {
            return new User(resultSet.getLong(User.Fields.id),
                            resultSet.getString(User.Fields.name),
                            resultSet.getInt(User.Fields.age));
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
  public long saveUser(User user) {
    try {
      return dbExecutor.insertRecord(getConnection(), jdbcSqlMaker.insertQuery(), List
              .of(user.getName(), String.valueOf(user.getAge())));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new UserDaoException(e);
    }
  }

  @Override
  public long updateUser(User user) {
    try {
      return dbExecutor.insertRecord(getConnection(),
                                     jdbcSqlMaker.updateQuery(),
                                     List.of(user.getName(),
                                             String.valueOf(user.getAge()),
                                             String.valueOf(user.getId())));
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
