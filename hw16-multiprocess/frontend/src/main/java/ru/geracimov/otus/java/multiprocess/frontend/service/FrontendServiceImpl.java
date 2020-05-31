package ru.geracimov.otus.java.multiprocess.frontend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import ru.geracimov.otus.java.multiprocess.frontend.config.FrontendProperties;
import ru.geracimov.otus.java.multiprocess.frontend.model.User;
import ru.geracimov.otus.java.multiprocess.frontend.ms.Message;
import ru.geracimov.otus.java.multiprocess.frontend.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.frontend.ms.MsClient;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService, CommandLineRunner {
    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final String recipientName;
    private final MsClient msClient;
    private final FrontendProperties frontendProperties;

    @Override
    public void run(String... args) {
        Message outMsg = msClient.produceMessage("registerer", new InetSocketAddress(frontendProperties.getHost(), frontendProperties.getPort()), MessageType.CLIENT_REGISTER);
        consumerMap.put(outMsg.getId(), System.out::println);
        msClient.sendMessage(outMsg);
    }

    @PreDestroy
    private void unregister() {
        Message outMsg = msClient.produceMessage("registerer", new InetSocketAddress(frontendProperties.getHost(), frontendProperties.getPort()), MessageType.CLIENT_UNREGISTER);
        consumerMap.put(outMsg.getId(), System.out::println);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getUserListData(Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(recipientName, null, MessageType.USER_LIST);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveUserData(User user, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(recipientName, user, MessageType.USER_SAVE);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            log.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }

}
