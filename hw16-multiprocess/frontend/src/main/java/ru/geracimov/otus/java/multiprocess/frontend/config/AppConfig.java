package ru.geracimov.otus.java.multiprocess.frontend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.geracimov.otus.java.multiprocess.frontend.ms.MsClient;
import ru.geracimov.otus.java.multiprocess.frontend.ms.SocketMsClient;
import ru.geracimov.otus.java.multiprocess.frontend.service.FrontendService;
import ru.geracimov.otus.java.multiprocess.frontend.service.FrontendServiceImpl;
import ru.geracimov.otus.java.multiprocess.frontend.socket.SocketClient;

@Configuration
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class AppConfig {
    private final MsProperties msProperties;
    private final FrontendProperties frontendProperties;

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

    @Bean
    public SocketClient socketClient(ObjectMapper objectMapper) {
        return new SocketClient(msProperties.getHost(), msProperties.getPort(), objectMapper);
    }

    @Bean
    public MsClient frontendMsClient(SocketClient socketClient, ObjectMapper objectMapper) {
        return new SocketMsClient(frontendProperties.getName(), socketClient, objectMapper);
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(msProperties.getRecipientName(), frontendMsClient, frontendProperties);
    }

}
