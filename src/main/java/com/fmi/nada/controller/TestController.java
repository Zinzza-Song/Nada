package com.fmi.nada.controller;

import com.fmi.nada.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    TestService testService;

    @GetMapping("/")
    public String main() {
        testService.set("rlarlarla");

        return "index";
    }

}
