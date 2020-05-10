package ru.geracimov.otus.java.multiprocess.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "server")
public class ServerPropertiesConfig {
    @Value("${port:8081}")
    private int port;

}
