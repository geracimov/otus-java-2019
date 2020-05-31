package ru.geracimov.otus.java.multiprocess.messagesystem.ms.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.RequestHandler;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UnregisterMsClientHandler implements RequestHandler {
    private final MessageSystem messageSystem;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Optional<Message> handle(Message message) {
        log.info("Received UnregisteringMsClient message: {}", message);
        messageSystem.removeClient(message.getFrom());
        final Message ok = new Message(message.getTo(), message.getFrom(), message.getId(),
                MessageType.CLIENT_UNREGISTER, objectMapper.writeValueAsBytes("OK"));
        return Optional.of(ok);
    }

}
