package ru.geracimov.otus.java.multiprocess.messagesystem.ms;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name", "remoteAddress"})
public class MsClientImpl implements MsClient {
    private final Map<MessageType, RequestHandler> handlers = new ConcurrentHashMap<>();
    private final String name;
    private final MessageSystem messageSystem;
    private final InetSocketAddress remoteAddress;
    private final ObjectMapper objectMapper;

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type, requestHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            log.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public boolean canHandle(MessageType msgType) {
        return handlers.containsKey(msgType);
    }

    @Override
    public void handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                log.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            log.error("msg:" + msg, ex);
        }
    }

    @SneakyThrows
    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType, objectMapper.writeValueAsBytes(data));
    }

}
