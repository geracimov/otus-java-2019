package ru.geracimov.otus.java.multiprocess.backend.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.geracimov.otus.java.multiprocess.backend.config.BackendProperties;
import ru.geracimov.otus.java.multiprocess.backend.model.RegisterDto;
import ru.geracimov.otus.java.multiprocess.backend.model.User;
import ru.geracimov.otus.java.multiprocess.backend.ms.Message;
import ru.geracimov.otus.java.multiprocess.backend.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.backend.ms.MsClient;
import ru.geracimov.otus.java.multiprocess.backend.repository.UserDaoException;
import ru.geracimov.otus.java.multiprocess.backend.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ru.geracimov.otus.java.multiprocess.backend.ms.MessageType.*;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class SocketServer implements CommandLineRunner {
    private final MsClient msClient;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final BackendProperties backendProperties;
    private final Map<MessageType, Function<Message, Message>> handlers = new HashMap<>();

    @PostConstruct
    @SuppressWarnings("unused")
    private void init() {
        handlers.put(USER_LIST, this::findAll);
        handlers.put(USER_SAVE, this::saveUser);
    }

    public void run(String... args) {
        new Thread(this::start, "SocketServerStarter").start();
        final InetSocketAddress callbackSocket = new InetSocketAddress(backendProperties.getHost(), backendProperties.getPort());
        RegisterDto dto = new RegisterDto(callbackSocket, USER_LIST, USER_SAVE);
        Message outMsg = msClient.produceMessage("registerer", dto, MessageType.CLIENT_REGISTER);
        msClient.sendMessage(outMsg);
    }

    @PreDestroy
    private void unregister() {
        final InetSocketAddress callbackSocket = new InetSocketAddress(backendProperties.getHost(), backendProperties.getPort());
        Message outMsg = msClient.produceMessage("registerer", callbackSocket, MessageType.CLIENT_UNREGISTER);
        msClient.sendMessage(outMsg);
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(backendProperties.getPort())) {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientSocketHandle(clientSocket);
                }
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

    @SneakyThrows
    private void clientSocketHandle(Socket clientSocket) {
        Message requestMessage;
        Message responseMessage;
        try (InputStreamReader is = new InputStreamReader(clientSocket.getInputStream())) {
            requestMessage = objectMapper.readValue(is, Message.class);
            try {
                responseMessage = handlers.getOrDefault(requestMessage.getType(), logResponse()).apply(requestMessage);
            } catch (UserDaoException e) {
                final String message = "Cannot process input message: " + e.getMessage();
                log.error(message);
                responseMessage = new Message(requestMessage.getTo(), requestMessage.getFrom(), requestMessage.getId(), ERROR, message.getBytes());
            }
            if (responseMessage != null) msClient.sendMessage(responseMessage);
        }
    }

    private Function<Message, Message> logResponse() {
        return m -> {
            log.info("Response on {} from {}: {}", m.getSourceMessageId(), m.getFrom(), m);
            return null;
        };
    }

    @SneakyThrows
    private Message saveUser(Message requestMessage) {
        final User user = objectMapper.readValue(requestMessage.getPayload(), User.class);
        System.out.println(user);
        final User saved = userService.saveUser(user);
        System.out.println(saved);
        final String savedBytes = objectMapper.writeValueAsString(saved);
        return new Message(requestMessage.getTo(), requestMessage.getFrom(), requestMessage.getId(), requestMessage.getType(), savedBytes.getBytes());
    }

    @SneakyThrows
    private Message findAll(Message requestMessage) {
        final List<User> users = userService.findAll();
        final String usersPayload = objectMapper.writeValueAsString(users);
        return new Message(requestMessage.getTo(), requestMessage.getFrom(), requestMessage.getId(), requestMessage.getType(), usersPayload.getBytes());
    }

}
