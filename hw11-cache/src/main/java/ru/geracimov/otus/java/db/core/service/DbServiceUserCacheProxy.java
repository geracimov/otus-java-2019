package ru.geracimov.otus.java.db.core.service;

import ru.geracimov.otus.java.cache.CacheManager;
import ru.geracimov.otus.java.cache.HwCache;
import ru.geracimov.otus.java.db.core.model.User;

import java.util.Optional;

public class DbServiceUserCacheProxy implements DBServiceUser {
    private final HwCache<Long, User> cache;
    private final DBServiceUser cachedService;

    public DbServiceUserCacheProxy(DBServiceUser dbServiceUser, CacheManager cacheManager) {
        cachedService = dbServiceUser;
        this.cache = cacheManager.createCache(this.getClass().getName(), Long.class, User.class);
    }

    @Override
    public long saveUser(User user) {
        final long id = cachedService.saveUser(user);
        cache.put(id, user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        User user;
        if ((user = cache.get(id)) != null)
            return Optional.of(user);
        return cachedService.getUser(id);
    }

    @Override
    public Optional<User> getUserWithAddress(long id) {
        User user;
        if ((user = cache.get(id)) != null)
            return Optional.of(user);
        return cachedService.getUser(id);
    }
}
