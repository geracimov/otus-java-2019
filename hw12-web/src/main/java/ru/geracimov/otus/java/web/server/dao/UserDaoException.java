package ru.geracimov.otus.java.web.server.dao;

public class UserDaoException extends RuntimeException {
    public UserDaoException(Exception ex) {
        super(ex);
    }

    public UserDaoException(String message) {
        super(message);
    }
}