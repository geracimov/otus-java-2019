package ru.geracimov.otus.java.multiprocess.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Getter
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final RedisPropertiesConfig config;

    @Bean
    public JedisPool jedis( ) {
        System.out.println( "jedisPort = " + config.getPort() );
        System.out.println( "jedisHost = " + config.getHost() );
        return new JedisPool( config.getHost(), config.getPort() );
    }

    @Bean
    public ObjectMapper objectMapper( ) {
        return new ObjectMapper();
    }

}
