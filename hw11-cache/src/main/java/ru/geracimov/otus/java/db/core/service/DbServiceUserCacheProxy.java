package ru.geracimov.otus.java.db.core.service;

import ru.geracimov.otus.java.cache.CacheManager;
import ru.geracimov.otus.java.cache.HwCache;
import ru.geracimov.otus.java.db.core.model.User;

import java.util.Optional;
import java.util.function.Function;

public class DbServiceUserCacheProxy implements DBServiceUser {
    public static final String LAZY = "LAZY";
    public static final String EAGER = "EAGER";
    private final HwCache<String, User> cache;
    private final DBServiceUser cachedService;

    public DbServiceUserCacheProxy(DBServiceUser dbServiceUser, CacheManager cacheManager) {
        cachedService = dbServiceUser;
        this.cache = cacheManager.createCache(this.getClass().getName(), String.class, User.class);
    }

    @Override
    public long saveUser(User user) {
        final long id = cachedService.saveUser(user);
        cache.put(createCacheKey(LAZY, user.getId()), user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        return getUser(id, cachedService::getUser, LAZY);
    }

    @Override
    public Optional<User> getUserWithAddress(long id) {
        return getUser(id, cachedService::getUserWithAddress, EAGER);
    }

    private Optional<User> getUser(long id, Function<Long, Optional<User>> userGetter, String prefix) {
        User user;
        if ((user = cache.get(createCacheKey(prefix, id))) != null)
            return Optional.of(user);
        final Optional<User> optionalUser = userGetter.apply(id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            cache.put(createCacheKey(prefix, user.getId()), user);
        }
        return optionalUser;
    }

    private String createCacheKey(String prefix, long id) {
        return String.format("%s:%s", prefix, id);
    }

}
