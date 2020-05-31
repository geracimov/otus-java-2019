package ru.geracimov.otus.java.multiprocess.frontend.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.frontend.ms.Message;

import java.io.BufferedOutputStream;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class SocketClient {
    private final String host;
    private final int port;
    private final ObjectMapper objectMapper;

    public void sendMessage(Message message) {
        log.info("Message {} was received from {} to {}", message.getId(), message.getFrom(), message.getTo());
        log.info("connected to the host {} on {} port", host, port);
        try (Socket socket = new Socket(host, port);
             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

            objectMapper.writeValue(bos, message);
            log.info("Message {} was sent to {}", message.getId(), message.getTo());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

}
