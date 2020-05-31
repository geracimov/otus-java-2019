package ru.geracimov.otus.java.multiprocess.messagesystem.ms.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.messagesystem.model.RegisterDto;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.*;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class RegisterMsClientHandler implements RequestHandler {
    private final MessageSystem messageSystem;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Optional<Message> handle(Message message) {
        log.info("Received RegistrationMsClient message: {}", message);
        final RegisterDto dto = objectMapper.readValue(message.getPayload(), RegisterDto.class);
        final MsClientImpl msClient = new MsClientImpl(message.getFrom(), messageSystem, dto.getAddress(), objectMapper);
        dto.getHandledTypes()
                .forEach(type -> msClient.addHandler(type, new SendToMsClientRequestHandler(messageSystem, objectMapper)));
        messageSystem.addClient(msClient);

        final Message ok = new Message(message.getTo(), message.getFrom(), message.getId(),
                MessageType.CLIENT_REGISTER, objectMapper.writeValueAsBytes("OK"));
        return Optional.of(ok);
    }
}
