package ru.geracimov.otus.java.multiprocess.messagesystem.ms;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Value
@EqualsAndHashCode(of = "id")
@ToString(exclude = "payload")
public class Message implements Serializable {
    static final Message VOID = new Message();

    UUID id = UUID.randomUUID();
    String from;
    String to;
    UUID sourceMessageId;
    MessageType type;
    int payloadLength;
    byte[] payload;

    private Message() {
        this.from = null;
        this.to = null;
        this.sourceMessageId = null;
        this.type = MessageType.VOID;
        this.payload = new byte[1];
        this.payloadLength = payload.length;
    }

    public Message(String from, String to, UUID sourceMessageId, MessageType type, byte[] payload) {
        this.from = from;
        this.to = to;
        this.sourceMessageId = sourceMessageId;
        this.type = type;
        this.payloadLength = payload.length;
        this.payload = payload;
    }

    public Optional<UUID> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }
}
