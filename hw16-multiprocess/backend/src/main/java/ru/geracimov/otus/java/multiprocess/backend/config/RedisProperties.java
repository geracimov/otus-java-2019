package ru.geracimov.otus.java.multiprocess.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    @Value("${port:6379}")
    private int port;

    @Value("${host:localhost}")
    private String host;

}
