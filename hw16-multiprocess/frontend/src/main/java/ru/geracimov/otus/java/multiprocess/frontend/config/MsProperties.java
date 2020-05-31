package ru.geracimov.otus.java.multiprocess.frontend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ms.server")
public class MsProperties {

    private String host;

    private int port;

    private String backendName;

}
