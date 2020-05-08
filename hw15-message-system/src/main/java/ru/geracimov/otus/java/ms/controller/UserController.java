package ru.geracimov.otus.java.ms.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.otusms.front.FrontendService;

@Slf4j
@Controller
@RequiredArgsConstructor
@SuppressWarnings({"SameReturnValue", "SpringMVCViewInspection"})
public class UserController {
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @GetMapping({"/users"})
    public String userListView() {
        return "users.html";
    }

    @MessageMapping({"/user"})
    public void getUsers() {
        frontendService.getUserListData(this::sendData);
    }

    @MessageMapping({"/user/save"})
    public void saveUser(User user) {
        frontendService.saveUserData(user, this::sendData);
    }

    @MessageExceptionHandler
    @SendTo("/ws/user/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    private void sendData(Boolean isError, String data) {
        log.info("got data:{}", data);
        String chanel = isError ? "/ws/user/errors" : "/ws/user/queue";
        template.convertAndSend(chanel, data);
    }

}
