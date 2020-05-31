package ru.geracimov.otus.java.multiprocess.frontend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.geracimov.otus.java.multiprocess.frontend.model.User;
import ru.geracimov.otus.java.multiprocess.frontend.service.FrontendService;

import java.util.function.Consumer;

@Slf4j
@Controller
@RequiredArgsConstructor
@SuppressWarnings({"SameReturnValue", "SpringMVCViewInspection"})
public class UserController {
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @GetMapping({"/users"})
    public String userListView(Model model) {
        return "users.html";
    }

    @MessageMapping({"/user"})
    public void getUsers() {
        System.out.println("get request /user");
        frontendService.getUserListData(data -> {
            log.info("got data:{}", data);
            template.convertAndSend("/ws/user/queue", data);
        });
    }

    @MessageMapping({"/user/save"})
    public void saveUser(User user) {
        System.out.println("get request /user/save");
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
