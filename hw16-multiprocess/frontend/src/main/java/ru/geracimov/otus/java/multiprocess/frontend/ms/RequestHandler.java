package ru.geracimov.otus.java.multiprocess.frontend.ms;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
