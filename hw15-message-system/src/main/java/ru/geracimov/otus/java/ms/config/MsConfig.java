package ru.geracimov.otus.java.ms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import ru.geracimov.otus.java.ms.otusms.db.handlers.GetUserDataRequestHandler;
import ru.geracimov.otus.java.ms.otusms.db.handlers.GetUserListDataRequestHandler;
import ru.geracimov.otus.java.ms.otusms.db.handlers.SaveUserDataRequestHandler;
import ru.geracimov.otus.java.ms.otusms.front.FrontendService;
import ru.geracimov.otus.java.ms.otusms.front.FrontendServiceImpl;
import ru.geracimov.otus.java.ms.otusms.front.handlers.GetUserDataResponseHandler;
import ru.geracimov.otus.java.ms.otusms.messagesystem.*;
import ru.geracimov.otus.java.ms.services.UserService;

@Configuration
public class MsConfig {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem, UserService userService, ObjectMapper objectMapper) {
        final MsClientImpl databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(userService, objectMapper));
        databaseMsClient.addHandler(MessageType.USER_LIST, new GetUserListDataRequestHandler(userService, objectMapper));
        databaseMsClient.addHandler(MessageType.USER_SAVE, new SaveUserDataRequestHandler(userService, objectMapper));
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    public FrontendService frontendService(@Lazy MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean
    public MsClient frontendMsClient(MessageSystem messageSystem, FrontendService frontendService) {
        final MsClientImpl frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.USER_LIST, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.USER_SAVE, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.USER_SAVE_ERROR, new GetUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

}
