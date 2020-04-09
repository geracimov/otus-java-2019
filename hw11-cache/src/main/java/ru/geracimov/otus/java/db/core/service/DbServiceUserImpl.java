package ru.geracimov.otus.java.db.core.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geracimov.otus.java.cache.HwCache;
import ru.geracimov.otus.java.db.core.dao.UserDao;
import ru.geracimov.otus.java.db.core.model.User;
import ru.geracimov.otus.java.db.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
  private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

  private final UserDao userDao;
  private HwCache<Long, User> cache;

  public DbServiceUserImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setCache(HwCache<Long, User> cache){
    assert cache != null;
    this.cache = cache;
  }

  @Override
  public boolean useCache(){
    return cache != null;
  }

  @Override
  public long saveUser(User user) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        long userId = userDao.saveUser(user);
        sessionManager.commitSession();

        logger.info("created user: {}", userId);
        if(useCache()) cache.put(userId, user);
        return userId;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<User> getUser(long id) {
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = userDao.findById(id);

        logger.info("user: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> getUserWithAddress(long id) {
    User user;
    if (useCache() && (user = cache.get(id)) != null){
      return Optional.of(user);
    }
    try (SessionManager sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = userDao.findById(id);
        user = userOptional.orElse(null);
        if (user!=null) {
          Hibernate.initialize(user.getAddressDataSet());
          if (useCache()) cache.put(id, user);
        }
        logger.info("user: {}", user);
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }
}
