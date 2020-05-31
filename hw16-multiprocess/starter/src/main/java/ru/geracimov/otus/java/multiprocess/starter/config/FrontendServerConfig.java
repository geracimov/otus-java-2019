package ru.geracimov.otus.java.multiprocess.starter.config;

import lombok.Data;

@Data
public class FrontendServerConfig {
    private String host;
    private int port;
    private int httpPort;
    private String recipientName;

}
