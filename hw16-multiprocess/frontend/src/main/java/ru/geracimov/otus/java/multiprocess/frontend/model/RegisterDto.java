package ru.geracimov.otus.java.multiprocess.frontend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geracimov.otus.java.multiprocess.frontend.ms.MessageType;

import java.net.InetSocketAddress;
import java.util.List;

@Data
@NoArgsConstructor
public class RegisterDto {
    private InetSocketAddress address;
    private List<MessageType> handledTypes;

    public RegisterDto(InetSocketAddress address, MessageType... messageTypes) {
        this.address = address;
        this.handledTypes = List.of(messageTypes);
    }

}
