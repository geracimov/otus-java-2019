package ru.geracimov.otus.java.orm.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.dao.Dao;
import ru.geracimov.otus.java.orm.dao.JdbcTemplate;
import ru.geracimov.otus.java.orm.example.User;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.serializer.service.SerializerServiceFacade;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

@Slf4j
public class UserDaoJdbc implements Dao<User> {
    private final JdbcTemplate<User> template;
    private final SessionManager sessionManager;

    public UserDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor, SerializerServiceFacade facade) {
        this.template = new SimpleJdbcTemplate<>(dbExecutor, sessionManager, facade);
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        log.debug("Getting User by Id {}", id);
        try {
            return template.load(id, User.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long save(User user) {
        log.debug("Saving {}", user);
        try {
            return template.create(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OrmException(e);
        }
    }

    @Override
    public void update(User user) {
        log.debug("Updating {}", user);
        try {
            template.update(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OrmException(e);
        }
    }

    @Override
    public void createOrUpdate(User user) {
        log.debug("Creating or updating {}", user);
        try {
            template.createOrUpdate(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OrmException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
