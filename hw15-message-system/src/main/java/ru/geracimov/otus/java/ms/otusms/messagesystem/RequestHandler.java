package ru.geracimov.otus.java.ms.otusms.messagesystem;


import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
