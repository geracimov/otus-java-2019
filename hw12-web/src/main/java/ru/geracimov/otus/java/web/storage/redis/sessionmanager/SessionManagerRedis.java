package ru.geracimov.otus.java.web.storage.redis.sessionmanager;


import lombok.NonNull;
import redis.clients.jedis.JedisPool;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.SessionManager;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.SessionManagerException;

public class SessionManagerRedis implements SessionManager {
    private final JedisPool jedisPool;

    public SessionManagerRedis(@NonNull JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void beginSession() {
    }

    @Override
    public void commitSession() {

    }

    @Override
    public void rollbackSession() {

    }

    @Override
    public void close() {

    }

    @Override
    public DatabaseSessionRedis getCurrentSession() {
        checkConnection();
        return new DatabaseSessionRedis(jedisPool.getResource());
    }

    private void checkConnection() {
        if (jedisPool == null || jedisPool.isClosed()) {
            throw new SessionManagerException("Pool is closed");
        }
    }
}
