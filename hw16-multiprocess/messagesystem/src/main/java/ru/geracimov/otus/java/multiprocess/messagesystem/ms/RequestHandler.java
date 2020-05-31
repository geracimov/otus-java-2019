package ru.geracimov.otus.java.multiprocess.messagesystem.ms;


import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
