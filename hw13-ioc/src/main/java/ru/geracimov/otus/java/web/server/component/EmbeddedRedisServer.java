package ru.geracimov.otus.java.web.server.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import java.io.IOException;

@Slf4j
@Component
@Conditional(EmbeddedRedisCondition.class)
public class EmbeddedRedisServer implements InitializingBean, DisposableBean {
    private final RedisServer redisServer;

    public EmbeddedRedisServer() throws IOException {
        this.redisServer = new RedisServer();
    }

    @Override
    public void afterPropertiesSet() {
        this.redisServer.start();
        log.info("Embedded Redis started");
    }

    @Override
    public void destroy() {
        this.redisServer.stop();
    }

}
