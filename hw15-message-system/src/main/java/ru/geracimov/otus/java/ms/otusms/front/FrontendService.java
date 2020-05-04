package ru.geracimov.otus.java.ms.otusms.front;


import ru.geracimov.otus.java.ms.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    void getUserData(long userId, Consumer<String> dataConsumer);

    void getUserListData(Consumer<String> dataConsumer);

    void saveUserData(User user, Consumer<String> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

