package ru.geracimov.otus.java.multiprocess.backend.ms;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Message {
    private final UUID id = UUID.randomUUID();
    private final String from;
    private final String to;
    private final UUID sourceMessageId;
    private final MessageType type;
    private final int payloadLength;
    private final byte[] payload;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", sourceMessageId=" + sourceMessageId +
                ", type='" + type + '\'' +
                ", payloadLength=" + payloadLength +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getPayloadLength() {
        return payloadLength;
    }


    public Optional<UUID> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }
}
