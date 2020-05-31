package ru.geracimov.otus.java.multiprocess.backend.ms;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
