package ru.geracimov.otus.java.multiprocess.frontend.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.geracimov.otus.java.multiprocess.frontend.config.FrontendProperties;
import ru.geracimov.otus.java.multiprocess.frontend.ms.Message;
import ru.geracimov.otus.java.multiprocess.frontend.service.FrontendService;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class SocketServer implements CommandLineRunner {
    private final FrontendService frontendService;
    private final FrontendProperties frontendProperties;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        new Thread(this::start, "SocketServerStarter").start();
    }

    public void start() {
        final int port = frontendProperties.getPort();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Starting frontendServerSocket on port: {}", port);

            while (!Thread.currentThread().isInterrupted()) {
                log.info("Waiting for client connection...");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try (InputStream is = clientSocket.getInputStream()) {
            final Message receivedMessage = objectMapper.readValue(is, Message.class);
//            Message receivedMessage = (Message) ois.readObject();
            log.info("Received from {} message ID[{}]", receivedMessage.getFrom(), receivedMessage.getId());
            frontendService.takeConsumer(receivedMessage.getSourceMessageId().orElse(null), String.class)
                    .ifPresent(stringConsumer -> stringConsumer.accept(new String(receivedMessage.getPayload())));
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }
}
