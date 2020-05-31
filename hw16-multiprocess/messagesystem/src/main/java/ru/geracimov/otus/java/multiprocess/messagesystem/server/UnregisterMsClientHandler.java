package ru.geracimov.otus.java.multiprocess.messagesystem.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;

import java.util.function.Function;


@Slf4j
@RequiredArgsConstructor
public class UnregisterMsClientHandler implements Function<Message, Message> {
    private final MessageSystem messageSystem;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Message apply(Message message) {
        log.info("Received UnregisteringMsClient message: {}", message);
        messageSystem.removeClient(message.getFrom());
        return new Message(message.getTo(), message.getFrom(), message.getId(),
                MessageType.CLIENT_UNREGISTER, objectMapper.writeValueAsBytes("OK"));
    }

}
