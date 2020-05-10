package ru.geracimov.otus.java.multiprocess.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.geracimov.otus.java.multiprocess.backend.config.ServerPropertiesConfig;
import ru.geracimov.otus.java.multiprocess.backend.model.MessageDto;
import ru.geracimov.otus.java.multiprocess.backend.model.MessageDtoType;
import ru.geracimov.otus.java.multiprocess.backend.model.User;
import ru.geracimov.otus.java.multiprocess.backend.repository.UserRepository;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class ServerNIO implements CommandLineRunner {
    private final ServerSocketChannel serverSocketChannel;
    private final ServerSocket serverSocket;
    private final Selector selector;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ServerPropertiesConfig config;

    public ServerNIO(UserRepository userRepository, ObjectMapper objectMapper, ServerPropertiesConfig config) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(config.getPort()));

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Start server");
        while (!Thread.currentThread().isInterrupted()) {
            log.info("waiting for client");
            if (!selector.isOpen()) {
                destroy();

            } else if (selector.select() > 0) {
                performIO(selector);
            }
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        log.info("Stop server");
        selector.close();
        serverSocket.close();
        serverSocketChannel.close();
        Thread.currentThread().interrupt();
    }

    private void performIO(Selector selector) throws IOException {
        log.info("something happened");
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            if (key.isAcceptable()) {
                acceptConnection(key, selector);
            } else if (key.isReadable()) {
                readWriteClient(key);
            }
        }
    }

    private void acceptConnection(SelectionKey key, Selector selector) {
        log.info("accept client connection");
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            log.info("Connection from: " + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.err.println("Unable to accept channel");
            e.printStackTrace();
            key.cancel();
        }
    }

    private void readWriteClient(SelectionKey selectionKey) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        final String requestFromClient = readRequestFromChannel(socketChannel, buffer);
        log.info("requestFromClient: {} ", requestFromClient);

        final MessageDto response = processClientRequest(requestFromClient);

        writeResponseToChannel(buffer, socketChannel, response);
    }

    private void writeResponseToChannel(ByteBuffer buffer, SocketChannel socketChannel, MessageDto responseMessage) throws IOException {
        try {
            String response = getString(responseMessage);
            for (byte b : response.getBytes()) {
                buffer.put(b);
                if (buffer.position() == buffer.limit()) {
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                }
            }

            if (buffer.hasRemaining()) {
                buffer.flip();
                socketChannel.write(buffer);
            }
        } catch (IOException e) {
            log.error("Error writing back bytes: {}", e.getMessage());
            socketChannel.close();
        }
    }

    private String readRequestFromChannel(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        StringBuilder inputBuffer = new StringBuilder(100);
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            String input = StandardCharsets.UTF_8.decode(buffer).toString();
            inputBuffer.append(input);
            buffer.clear();
        }
        return inputBuffer.toString();
    }

    @SneakyThrows
    private MessageDto processClientRequest(String input) {
        MessageDto responseMessage = null;
        try {
            final MessageDto requestMessage = getMessage(input);
            switch (requestMessage.getType()) {
                case USER_LIST:
                    final List<User> users = userRepository.findAll();
                    final String usersPayload = objectMapper.writeValueAsString(users);
                    responseMessage = new MessageDto(requestMessage.getType(), usersPayload.getBytes(), usersPayload.length());
                    break;
                case USER_SAVE:
                    final User user = objectMapper.readValue(requestMessage.getPayload(), User.class);
                    System.out.println(user);
                    final User saved = userRepository.saveUser(user);
                    System.out.println(saved);
                    final String savedBytes = objectMapper.writeValueAsString(saved);
                    responseMessage = new MessageDto(requestMessage.getType(), savedBytes.getBytes(), savedBytes.length());
                    break;
                case VOID:
                    destroy();
                    break;
            }
            return responseMessage;
        } catch (Exception e) {
            final String message = "Cannot process input message: " + e.getMessage();
            log.error(message);
            responseMessage = new MessageDto(MessageDtoType.ERROR, message.getBytes(), message.length());
        }
        return responseMessage;
    }

    @SneakyThrows
    private MessageDto getMessage(String input) {
        return objectMapper.readValue(input, MessageDto.class);
    }

    @SneakyThrows
    private String getString(MessageDto message) {
        return objectMapper.writeValueAsString(message) + "\r\n";
    }

}
