package ru.geracimov.otus.java.ms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class CustomConfig {
    @Value("${redis.port:6379}")
    private int redisPort;
    @Value("${redis.host:localhost}")
    private String redisHost;

    @Bean
    public JedisPool jedis() {
        System.out.println("jedisPort = " + redisPort);
        System.out.println("jedisHost = " + redisHost);
        return new JedisPool(redisHost, redisPort);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
