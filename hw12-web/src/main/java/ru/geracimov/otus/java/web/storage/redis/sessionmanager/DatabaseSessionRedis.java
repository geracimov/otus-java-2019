package ru.geracimov.otus.java.web.storage.redis.sessionmanager;


import redis.clients.jedis.Jedis;
import ru.geracimov.otus.java.web.storage.core.sessionmanager.DatabaseSession;

public class DatabaseSessionRedis implements DatabaseSession {
    private final Jedis jedis;

    DatabaseSessionRedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public Jedis getJedis() {
        return jedis;
    }
}
