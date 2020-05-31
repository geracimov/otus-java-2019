package ru.geracimov.otus.java.multiprocess.messagesystem.ms.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.Message;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MsClient;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.RequestHandler;
import ru.geracimov.otus.java.multiprocess.messagesystem.server.SocketClient;

import java.net.InetSocketAddress;
import java.util.Optional;


@RequiredArgsConstructor
public class SendToMsClientRequestHandler implements RequestHandler {
    private final MessageSystem messageSystem;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Message> handle(Message message) {
        final MsClient msClient = messageSystem.getMsClient(message);
        final InetSocketAddress remoteAddress = msClient.getRemoteAddress();
        SocketClient socketClient = new SocketClient(remoteAddress.getHostName(), remoteAddress.getPort(), objectMapper);
        socketClient.sendMessage(message);
        return Optional.empty();
    }

}
