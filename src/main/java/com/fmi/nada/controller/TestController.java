package com.fmi.nada.controller;

import com.fmi.nada.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/")
    public String main() {
        testService.saveTest("test124");

        return "index";
    }

}
