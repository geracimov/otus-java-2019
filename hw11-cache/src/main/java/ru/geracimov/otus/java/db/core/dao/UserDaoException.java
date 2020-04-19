package ru.geracimov.otus.java.db.core.dao;

public class UserDaoException extends RuntimeException {
  public UserDaoException(Exception ex) {
    super(ex);
  }
}
