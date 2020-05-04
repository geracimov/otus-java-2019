package ru.geracimov.otus.java.ms.otusms.db.handlers;

import ru.geracimov.otus.java.ms.otusms.common.Serializers;
import ru.geracimov.otus.java.ms.otusms.db.DBService;
import ru.geracimov.otus.java.ms.otusms.messagesystem.Message;
import ru.geracimov.otus.java.ms.otusms.messagesystem.MessageType;
import ru.geracimov.otus.java.ms.otusms.messagesystem.RequestHandler;

import java.util.Optional;


public class GetUserDataRequestHandler implements RequestHandler {
    private final DBService dbService;

    public GetUserDataRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        long id = Serializers.deserialize(msg.getPayload(), Long.class);
        String data = dbService.getUserData(id);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.USER_DATA.getValue(), Serializers.serialize(data)));
    }
}
