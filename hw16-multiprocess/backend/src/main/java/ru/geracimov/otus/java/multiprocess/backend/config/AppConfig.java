package ru.geracimov.otus.java.multiprocess.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final RedisPropertiesConfig config;

    @Bean
    public JedisPool jedis() {
        log.info("jedisPort = " + config.getPort());
        log.info("jedisHost = " + config.getHost());
        return new JedisPool(config.getHost(), config.getPort());
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

}
