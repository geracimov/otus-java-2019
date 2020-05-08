package ru.geracimov.otus.java.ms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.otusms.db.handlers.GetUserDataRequestHandler;
import ru.geracimov.otus.java.ms.otusms.db.handlers.SaveUserDataRequestHandler;
import ru.geracimov.otus.java.ms.otusms.front.FrontendService;
import ru.geracimov.otus.java.ms.otusms.front.FrontendServiceImpl;
import ru.geracimov.otus.java.ms.otusms.front.handlers.GetUserDataResponseHandler;
import ru.geracimov.otus.java.ms.otusms.messagesystem.*;
import ru.geracimov.otus.java.ms.services.UserService;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageSystemApplicationTest {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemApplicationTest.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private MessageSystem messageSystem;
    private FrontendService frontendService;
    private MsClient frontendMsClient;
    private ObjectMapper objectMapper;

    @DisplayName("Сохранение пользователей")
    @RepeatedTest(10)
    public void saveUsersTest() throws Exception {
        createMessageSystem(true);
        int counter = 3;
        CountDownLatch waitLatch = new CountDownLatch(counter);

        IntStream.range(0, counter).forEach(id -> frontendService
                .saveUserData(createNewUser(),
                              (isError, data) -> {
                                  assertFalse(isError);
                                  waitLatch.countDown();
                              }));

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    private User createUser(long id) {
        return new User(id, "name" + id, "login" + id, "password" + id);
    }

    private User createNewUser() {
        return new User(0, "name", "login", "password");
    }

    @DisplayName("Базовый сценарий получения данных")
    @RepeatedTest(10)
    public void getUserTest() throws Exception {
        createMessageSystem(true);
        int counter = 3;
        CountDownLatch waitLatch = new CountDownLatch(counter);

        IntStream.range(0, counter).forEach(id -> frontendService.getUserData(id, (isError, data) -> {
            assertFalse(isError);
            try {
                final User user = objectMapper.readValue(data, User.class);
                assertThat(user.getId()).isEqualTo(id);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            waitLatch.countDown();
        }));

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Выполнение запроса после остановки сервиса")
    @RepeatedTest(10)
    public void getDataAfterShutdownTest() throws Exception {
        createMessageSystem(true);
        messageSystem.dispose();

        CountDownLatch waitLatchShutdown = new CountDownLatch(1);

        when(frontendMsClient.sendMessage(any(Message.class)))
                .thenAnswer(invocation -> {
                    waitLatchShutdown.countDown();
                    return null;
                });

        frontendService.getUserData(5, (isError, data) -> logger.info("data:{}", data));
        waitLatchShutdown.await();
        boolean result = verify(frontendMsClient).sendMessage(any(Message.class));
        assertThat(result).isFalse();

        logger.info("done");
    }

    @DisplayName("Тестируем остановку работы MessageSystem")
    @RepeatedTest(10)
    public void stopMessageSystemTest() throws Exception {
        createMessageSystem(false);
        int counter = 100;
        CountDownLatch messagesSentLatch = new CountDownLatch(counter);
        CountDownLatch messageSystemDisposed = new CountDownLatch(1);

        IntStream.range(0, counter).forEach(id -> {
                                                frontendService.getUserData(id, (isError, data) -> {
                                                });
                                                messagesSentLatch.countDown();
                                            }
        );
        messagesSentLatch.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(counter);

        messageSystem.start();
        disposeMessageSystem(messageSystemDisposed::countDown);

        messageSystemDisposed.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(0);

        logger.info("done");
    }


    private void createMessageSystem(boolean startProcessing) {
        logger.info("setup");
        messageSystem = new MessageSystemImpl(startProcessing);

        MsClient databaseMsClient = spy(new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem));
        UserService userService = mock(UserService.class);
        objectMapper = new ObjectMapper();
        when(userService.findById(any(Long.class)))
                .thenAnswer(invocation -> Optional.of(createUser(invocation.getArgument(0))));
        when(userService.saveUser(any(User.class)))
                .thenReturn(1L, 2L, 3L);
        databaseMsClient.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(userService, objectMapper));
        databaseMsClient.addHandler(MessageType.USER_SAVE, new SaveUserDataRequestHandler(userService, objectMapper));

        messageSystem.addClient(databaseMsClient);

        frontendMsClient = spy(new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem));
        frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.USER_SAVE_ERROR, new GetUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        logger.info("setup done");
    }

    private void disposeMessageSystem(Runnable callback) {
        try {
            messageSystem.dispose(callback);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}