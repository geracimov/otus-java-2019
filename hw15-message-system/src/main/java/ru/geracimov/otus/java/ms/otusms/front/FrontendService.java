package ru.geracimov.otus.java.ms.otusms.front;


import ru.geracimov.otus.java.ms.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface FrontendService {
    void getUserData(long userId, BiConsumer<Boolean, String> dataConsumer);

    void getUserListData(BiConsumer<Boolean, String> dataConsumer);

    void saveUserData(User user, BiConsumer<Boolean, String> dataConsumer);

    <E extends Boolean, T> Optional<BiConsumer<E, T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

