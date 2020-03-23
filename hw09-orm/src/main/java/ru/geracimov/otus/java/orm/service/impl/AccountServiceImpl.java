package ru.geracimov.otus.java.orm.service.impl;


import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.dao.Dao;
import ru.geracimov.otus.java.orm.example.Account;
import ru.geracimov.otus.java.orm.exception.OrmException;
import ru.geracimov.otus.java.orm.service.Service;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements Service<Account> {
    private final Dao<Account> accountDao;

    @Override
    public long save(Account account) {
        @Cleanup
        SessionManager sessionManager = accountDao.getSessionManager();
        sessionManager.beginSession();
        try {
            long accountId = accountDao.save(account);
            sessionManager.commitSession();
            log.info("created account: {}", accountId);
            return accountId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
            throw new OrmException(e);
        }
    }

    @Override
    public Optional<Account> getById(long id) {
        @Cleanup
        SessionManager sessionManager = accountDao.getSessionManager();
        sessionManager.beginSession();
        try {
            Optional<Account> accountOptional = accountDao.findById(id);
            log.info("GET account: {}", accountOptional.orElse(null));
            return accountOptional;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
        return Optional.empty();
    }

    @Override
    public void update(Account account) {
        @Cleanup
        SessionManager sessionManager = accountDao.getSessionManager();
        sessionManager.beginSession();
        try {
            accountDao.update(account);
            sessionManager.commitSession();
            log.info("UPD account: {}", account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
    }

    @Override
    public void createOrUpdate(Account account) {
        @Cleanup
        SessionManager sessionManager = accountDao.getSessionManager();
        sessionManager.beginSession();
        try {
            accountDao.createOrUpdate(account);
            sessionManager.commitSession();
            log.info("UPD account: {}", account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
    }

}
