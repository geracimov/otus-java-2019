package ru.geracimov.otus.java.multiprocess.messagesystem.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import ru.geracimov.otus.java.multiprocess.messagesystem.config.MsServerProperties;
import ru.geracimov.otus.java.multiprocess.messagesystem.model.RegisterDto;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.RequestHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Profile("!test")
@RequiredArgsConstructor
public class SocketServer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final MessageSystem messageSystem;
    private final MsServerProperties serverProperties;
    private final Map<MessageType, RequestHandler> handlers = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) {
        start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(serverProperties.getPort())) {
            log.info("Socket server was started on port {}", serverProperties.getPort());
            while (!Thread.currentThread().isInterrupted()) {
                log.info("Waiting for client connection...");
                try (Socket clientSocket = serverSocket.accept()) {
                    log.info("Connection accepted from {}", clientSocket.getRemoteSocketAddress());
                    clientHandle(clientSocket);
                }
            }
        } catch (Exception ex) {
            log.error("ServerSocket error", ex);
        }
    }

    public void addHandler(MessageType messageType, RequestHandler handler) {
        handlers.put(messageType, handler);
    }

    private void clientHandle(Socket clientSocket) {
        try (BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream())) {
            final Message message = objectMapper.readValue(bis.readAllBytes(), Message.class);
            log.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
            if (handlers.containsKey(message.getType())) {
                handlers.get(message.getType()).handle(message).ifPresent(new MessageConsumer(message));
            } else {
                messageSystem.newMessage(message);
            }
        } catch (Exception e) {
            log.error("Socket error", e);
        }
    }

    @RequiredArgsConstructor
    private class MessageConsumer implements Consumer<Message> {
        private final Message inputMessage;

        @Override
        @SneakyThrows
        public void accept(Message response) {
            final RegisterDto dto = objectMapper.readValue(inputMessage.getPayload(), RegisterDto.class);
            log.info(dto.toString());
            final Socket socket = new Socket(dto.getAddress().getHostName(), dto.getAddress().getPort());
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            objectMapper.writeValue(bos, response);
            bos.close();
        }

    }

}
