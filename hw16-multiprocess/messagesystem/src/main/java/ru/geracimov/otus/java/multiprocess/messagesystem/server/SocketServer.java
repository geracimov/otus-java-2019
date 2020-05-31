package ru.geracimov.otus.java.multiprocess.messagesystem.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import ru.geracimov.otus.java.multiprocess.messagesystem.config.MsServerProperties;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class SocketServer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final MessageSystem messageSystem;
    private final MsServerProperties serverProperties;
    private final Map<MessageType, Function<Message, Message>> handlers = new ConcurrentHashMap<>();

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

    public void addHandler(MessageType messageType, Function<Message, Message> handler) {
        handlers.put(messageType, handler);
    }

    private void clientHandle(Socket clientSocket) {
        try (BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream())) {
            final Message message = objectMapper.readValue(bis.readAllBytes(), Message.class);
            log.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
            if (handlers.containsKey(message.getType())) {
                final Message response = handlers.get(message.getType()).apply(message);
                final InetSocketAddress inetSocketAddress = objectMapper.readValue(message.getPayload(), InetSocketAddress.class);
                log.info(inetSocketAddress.toString());
                final Socket socket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                objectMapper.writeValue(bos, response);
                bos.close();
            } else {
                messageSystem.newMessage(message);
            }
        } catch (Exception e) {
            log.error("Socket error", e);
        }
    }

}
