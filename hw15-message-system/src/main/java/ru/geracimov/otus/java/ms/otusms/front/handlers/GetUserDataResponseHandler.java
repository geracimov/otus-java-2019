package ru.geracimov.otus.java.ms.otusms.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geracimov.otus.java.ms.otusms.common.Serializers;
import ru.geracimov.otus.java.ms.otusms.front.FrontendService;
import ru.geracimov.otus.java.ms.otusms.messagesystem.Message;
import ru.geracimov.otus.java.ms.otusms.messagesystem.MessageType;
import ru.geracimov.otus.java.ms.otusms.messagesystem.RequestHandler;

import java.util.Optional;
import java.util.UUID;

public class GetUserDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    private final FrontendService frontendService;

    public GetUserDataResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            String userData = Serializers.deserialize(msg.getPayload(), String.class);
            UUID sourceMessageId = msg.getSourceMessageId()
                                      .orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg
                                              .getId()));
            frontendService.takeConsumer(sourceMessageId, String.class)
                           .ifPresent(consumer -> consumer.accept(messageIsError(msg), userData));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }

    private boolean messageIsError(Message msg) {
        return MessageType.USER_SAVE_ERROR.getValue().equals(msg.getType());
    }

}
