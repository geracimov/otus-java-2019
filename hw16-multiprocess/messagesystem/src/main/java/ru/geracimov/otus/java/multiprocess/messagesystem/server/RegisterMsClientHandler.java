package ru.geracimov.otus.java.multiprocess.messagesystem.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MsClientImpl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Function;


@Slf4j
@RequiredArgsConstructor
public class RegisterMsClientHandler implements Function<Message, Message> {
    private final MessageSystem messageSystem;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Message apply(Message message) {
        log.info("Received RegistrationMsClient message: {}", message);
        final SocketAddress socketAddress = objectMapper.readValue(message.getPayload(), InetSocketAddress.class);
        final MsClientImpl msClient = new MsClientImpl(message.getFrom(), messageSystem, socketAddress, objectMapper);
        messageSystem.addClient(msClient);

        return new Message(message.getTo(), message.getFrom(), message.getId(),
                MessageType.CLIENT_REGISTER, objectMapper.writeValueAsBytes("OK"));
    }

}
