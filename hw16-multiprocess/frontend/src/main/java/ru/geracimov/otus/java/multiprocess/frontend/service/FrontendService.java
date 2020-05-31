package ru.geracimov.otus.java.multiprocess.frontend.service;

import ru.geracimov.otus.java.multiprocess.frontend.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {

    void getUserListData(Consumer<String> dataConsumer);

    void saveUserData(User user, Consumer<String> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

