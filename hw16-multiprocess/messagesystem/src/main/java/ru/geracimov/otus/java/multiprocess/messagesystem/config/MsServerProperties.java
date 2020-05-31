package ru.geracimov.otus.java.multiprocess.messagesystem.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ms.server")
public class MsServerProperties {

    private int port;

}
