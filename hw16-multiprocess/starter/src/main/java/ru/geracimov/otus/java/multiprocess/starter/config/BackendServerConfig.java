package ru.geracimov.otus.java.multiprocess.starter.config;

import lombok.Data;

@Data
public class BackendServerConfig {
    private String host;
    private int port;
    private String redisHost;
    private int redisPort;

}
