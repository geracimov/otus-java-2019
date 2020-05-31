package ru.geracimov.otus.java.multiprocess.starter.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.geracimov.otus.java.multiprocess.starter.config.ApplicationProperties;
import ru.geracimov.otus.java.multiprocess.starter.config.BackendServerConfig;
import ru.geracimov.otus.java.multiprocess.starter.config.FrontendServerConfig;
import ru.geracimov.otus.java.multiprocess.starter.config.MessageServerConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class Starter implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final List<Process> processes = new ArrayList<>();

    @SneakyThrows
    @Override
    public void run(String... args) {
        final MessageServerConfig msConfig = applicationProperties.getMessageServer();
        final File currentDirectory = new File("./hw16-multiprocess");

        final ProcessBuilder messageSystem = new ProcessBuilder("java", "-jar", "messagesystem/target/messagesystem-1.0-SNAPSHOT.jar",
                "-Dms.server.port=" + msConfig.getPort());
        processes.add(messageSystem.directory(currentDirectory).start());
        sleep(2500L);

        applicationProperties.getBackendServers().forEach(new BackendRunner(msConfig, currentDirectory));
        applicationProperties.getFrontendServers().forEach(new FrontendRunner(msConfig, currentDirectory));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            processes.forEach(Process::destroy);
        }
    }

    @SneakyThrows
    private void sleep(Long time) {
        Thread.sleep(time);
    }

    @RequiredArgsConstructor
    private class FrontendRunner implements BiConsumer<String, FrontendServerConfig> {
        private final MessageServerConfig msConfig;
        private final File currentDirectory;

        @SneakyThrows
        @Override
        public void accept(String serverName, FrontendServerConfig config) {
            log.info("Starting {} with {}", serverName, config);
            ProcessBuilder frontendServer = new ProcessBuilder("java", "-jar", "frontend/target/frontend-1.0-SNAPSHOT.jar");
//                    "-Dserver.port=" + config.getHttpPort());
            final Map<String, String> environment = frontendServer.environment();
            environment.put("FRONTEND_NAME", serverName);
            environment.put("FRONTEND_HOST", config.getHost());
            environment.put("FRONTEND_PORT", String.valueOf(config.getPort()));
            environment.put("HTTP_SERVER_PORT", String.valueOf(config.getHttpPort()));
            environment.put("RECIPIENT_NAME", config.getRecipientName());
            environment.put("MS_HOST", msConfig.getHost());
            environment.put("MS_PORT", String.valueOf(msConfig.getPort()));

            final Process started = frontendServer.directory(currentDirectory).start();
            log.info("Started PID {}", started.pid());
            processes.add(started);
            sleep(1000L);
        }
    }

    @RequiredArgsConstructor
    private class BackendRunner implements BiConsumer<String, BackendServerConfig> {
        private final MessageServerConfig msConfig;
        private final File currentDirectory;

        @SneakyThrows
        @Override
        public void accept(String serverName, BackendServerConfig config) {
            log.info("Starting {} with {}", serverName, config);
            ProcessBuilder backendServer = new ProcessBuilder("java", "-jar", "backend/target/backend-1.0-SNAPSHOT.jar",
                    "-Dredis.host=" + config.getRedisHost(),
                    "-Dredis.port=" + config.getRedisPort());
            final Map<String, String> environment = backendServer.environment();
            environment.put("BACKEND_NAME", serverName);
            environment.put("BACKEND_HOST", config.getHost());
            environment.put("BACKEND_PORT", String.valueOf(config.getPort()));
            environment.put("MS_HOST", msConfig.getHost());
            environment.put("MS_PORT", String.valueOf(msConfig.getPort()));
            final Process started = backendServer.directory(currentDirectory).start();
            log.info("Started PID {}", started.pid());
            processes.add(started);
            sleep(1000L);
        }
    }

}
