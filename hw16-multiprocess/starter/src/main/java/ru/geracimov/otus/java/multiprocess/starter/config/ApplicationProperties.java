package ru.geracimov.otus.java.multiprocess.starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties("config")
public class ApplicationProperties {


    private MessageServerConfig messageServer;
    private Map<String, BackendServerConfig> backendServers;
    private Map<String, FrontendServerConfig> frontendServers;

    @PostConstruct
    private void dfd() {
        System.out.println(messageServer);
        System.out.println(backendServers);
        System.out.println(frontendServers);
    }

}
