package ru.geracimov.otus.java.multiprocess.backend.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MessageDto {
    private final MessageDtoType type;
    private final byte[] payload;
    private final int payloadLength;

    public MessageDto() {
        this.type = null;
        this.payloadLength = 0;
        this.payload = null;
    }

}
