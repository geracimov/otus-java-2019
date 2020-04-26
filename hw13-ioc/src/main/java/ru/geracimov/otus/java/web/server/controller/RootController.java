package ru.geracimov.otus.java.web.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("SameReturnValue")
@Controller
public class RootController {

    @GetMapping("/")
    public String root(){
        return "index";
    }

}
