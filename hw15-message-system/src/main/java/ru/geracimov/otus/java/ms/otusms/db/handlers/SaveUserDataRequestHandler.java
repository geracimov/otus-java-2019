package ru.geracimov.otus.java.ms.otusms.db.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.otusms.common.Serializers;
import ru.geracimov.otus.java.ms.otusms.messagesystem.Message;
import ru.geracimov.otus.java.ms.otusms.messagesystem.MessageType;
import ru.geracimov.otus.java.ms.otusms.messagesystem.RequestHandler;
import ru.geracimov.otus.java.ms.repository.UserDaoException;
import ru.geracimov.otus.java.ms.services.UserService;

import java.util.Optional;


public class SaveUserDataRequestHandler implements RequestHandler {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public SaveUserDataRequestHandler(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        try {
            userService.saveUser(user);
            final String value = objectMapper.writeValueAsString(user);
            return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(value)));
        } catch (UserDaoException e) {
            return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_SAVE_ERROR.getValue(), Serializers.serialize(e.getMessage())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
