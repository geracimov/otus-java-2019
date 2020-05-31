package ru.geracimov.otus.java.multiprocess.messagesystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystem;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageSystemImpl;
import ru.geracimov.otus.java.multiprocess.messagesystem.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.messagesystem.server.SocketServer;
import ru.geracimov.otus.java.multiprocess.messagesystem.server.RegisterMsClientHandler;
import ru.geracimov.otus.java.multiprocess.messagesystem.server.UnregisterMsClientHandler;

@Configuration
public class MsConfig {
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public SocketServer msServerSocket(ObjectMapper objectMapper, MsServerProperties msServerPropertiesConfig, MessageSystem messageSystem) {
        final SocketServer msServerSocket = new SocketServer(objectMapper,messageSystem, msServerPropertiesConfig);
        msServerSocket.addHandler(MessageType.CLIENT_REGISTER, new RegisterMsClientHandler(messageSystem, objectMapper));
        msServerSocket.addHandler(MessageType.CLIENT_UNREGISTER, new UnregisterMsClientHandler(messageSystem, objectMapper));
        return msServerSocket;
    }

}
