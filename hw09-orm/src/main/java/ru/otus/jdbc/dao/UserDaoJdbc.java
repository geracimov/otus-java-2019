package ru.otus.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDaoException;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.query.MetaDataSource;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {
  private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

  private final SessionManagerJdbc sessionManager;
  private final DbExecutor<User> dbExecutor;
  private final MetaDataSource metaData;
  private final JdbcMapper jdbcMapper;

  public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor,
                     MetaDataSource metaData, JdbcMapper jdbcMapper) {
    this.sessionManager = sessionManager;
    this.dbExecutor = dbExecutor;
    this.metaData = metaData;
    this.jdbcMapper=jdbcMapper;
  }


  @Override
  public Optional<User> findById(long id) {
    try {
      return dbExecutor.selectRecord(getConnection(), metaData.getSelectQuery(User.class), id, jdbcMapper.map(User.class));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }


  @Override
  public long saveUser(User user) {
    try {
      return dbExecutor.insertRecord(getConnection(), metaData.getInsertQuery(User.class), List
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
                                     metaData.getUpdateQuery(User.class),
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
