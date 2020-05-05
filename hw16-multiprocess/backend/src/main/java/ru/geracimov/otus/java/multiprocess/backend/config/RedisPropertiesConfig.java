package ru.geracimov.otus.java.multiprocess.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties( prefix = "redis" )
public class RedisPropertiesConfig {
    private int port;
    private String host;

}
