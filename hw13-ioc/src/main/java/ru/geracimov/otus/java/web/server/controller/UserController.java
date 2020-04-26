package ru.geracimov.otus.java.web.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.geracimov.otus.java.web.server.model.User;
import ru.geracimov.otus.java.web.server.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@SuppressWarnings({"SameReturnValue", "SpringMVCViewInspection"})
public class UserController {
    private final UserService userService;

    @GetMapping({"/users"})
    public String userListView(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        return "users.html";
    }

    @PostMapping("/users")
    public RedirectView userSave(@ModelAttribute User user) {
        userService.saveUser(user);
        return new RedirectView("/users", true);
    }

}
