package ru.geracimov.otus.java.ms.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;
import ru.geracimov.otus.java.ms.config.CustomConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Component
@Conditional(EmbeddedRedisCondition.class)
public class EmbeddedRedisServer {
    private final RedisServer redisServer;

    public EmbeddedRedisServer(CustomConfig customConfig) throws IOException {
        this.redisServer = new RedisServer(customConfig.getRedisPort());
    }

    @PostConstruct
    public void afterPropertiesSet() {
        log.info("Embedded Redis start");
        this.redisServer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("Embedded Redis stop");
        this.redisServer.stop();
    }

}
