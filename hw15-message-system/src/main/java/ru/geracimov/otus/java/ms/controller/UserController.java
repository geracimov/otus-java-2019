package ru.geracimov.otus.java.ms.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.otusms.front.FrontendService;
import ru.geracimov.otus.java.ms.services.UserService;

import java.util.function.Consumer;

@Slf4j
@Controller
@RequiredArgsConstructor
@SuppressWarnings({"SameReturnValue", "SpringMVCViewInspection"})
public class UserController {
    private final UserService userService;
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @GetMapping({"/users"})
    public String userListView(Model model) {
        return "users.html";
    }

    @MessageMapping({"/user"})
    public void getUsers() {
        frontendService.getUserListData(data -> {
            log.info("got data:{}", data);
            template.convertAndSend("/ws/user/queue", data);
        });
    }

    @MessageMapping({"/user/save"})
    public void saveUser(User user) {
        final Consumer<String> dataConsumer = data -> {
            log.info("got data:{}", data);
            template.convertAndSend("/ws/user/queue", data);
        };
        frontendService.saveUserData(user, dataConsumer);
    }

    @MessageExceptionHandler
    @SendTo("/ws/user/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
