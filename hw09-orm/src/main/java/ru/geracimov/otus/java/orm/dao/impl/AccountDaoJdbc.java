package ru.geracimov.otus.java.orm.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.dao.Dao;
import ru.geracimov.otus.java.orm.dao.JdbcTemplate;
import ru.geracimov.otus.java.orm.example.Account;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.serializer.service.SerializerServiceFacade;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

@Slf4j
public class AccountDaoJdbc implements Dao<Account> {
    private final JdbcTemplate<Account> template;
    private final SessionManager sessionManager;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, DbExecutor<Account> dbExecutor, SerializerServiceFacade facade) {
        this.template = new SimpleJdbcTemplate<>(dbExecutor, sessionManager, facade);
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Account> findById(long id) {
        log.debug("Getting Account by Id {}", id);
        try {
            return template.load(id, Account.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long save(Account Account) {
        log.debug("Saving {}", Account);
        try {
            return template.create(Account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OrmException(e);
        }
    }

    @Override
    public void update(Account Account) {
        log.debug("Updating {}", Account);
        try {
            template.update(Account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OrmException(e);
        }
    }

    @Override
    public void createOrUpdate(Account Account) {
        log.debug("Creating or updating {}", Account);
        try {
            template.createOrUpdate(Account);
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
