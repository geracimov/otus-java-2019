package ru.geracimov.otus.java.ms.otusms.db.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.otusms.common.Serializers;
import ru.geracimov.otus.java.ms.otusms.messagesystem.Message;
import ru.geracimov.otus.java.ms.otusms.messagesystem.MessageType;
import ru.geracimov.otus.java.ms.otusms.messagesystem.RequestHandler;
import ru.geracimov.otus.java.ms.services.UserService;

import java.util.List;
import java.util.Optional;


public class GetUserListDataRequestHandler implements RequestHandler {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public GetUserListDataRequestHandler(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final List<User> users = userService.findAll();
        try {
            final String value = objectMapper.writeValueAsString(users);
            return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_LIST.getValue(), Serializers.serialize(value)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
