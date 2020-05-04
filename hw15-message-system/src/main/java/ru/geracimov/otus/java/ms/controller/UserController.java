package ru.geracimov.otus.java.ms.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.services.UserService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@SuppressWarnings({"SameReturnValue", "SpringMVCViewInspection"})
public class UserController {
    private final UserService userService;

    @GetMapping({"/users"})
    public String userListView(Model model) {
        return "users.html";
    }

    @MessageMapping({"/user"})
    @SendTo({"/ws/user/queue"})
    public List<User> getUsers() {
        return userService.findAll();
    }

    @MessageMapping({"/user/save"})
    @SendTo({"/ws/user/queue"})
    public User saveUser(User user) {
        final long id = userService.saveUser(user);
        user.setId(id);
        return user;
    }

    @MessageExceptionHandler
    @SendTo("/ws/user/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
