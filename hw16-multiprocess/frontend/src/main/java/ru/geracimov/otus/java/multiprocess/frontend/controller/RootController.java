package ru.geracimov.otus.java.multiprocess.frontend.controller;

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
