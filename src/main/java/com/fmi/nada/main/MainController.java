package com.fmi.nada.main;

import com.fmi.nada.diary.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    /**
     * 오늘의 조언 랜덤 조회
     *
     * @return 메인 페이지 view("/index.html")
     */
    @GetMapping("/")
    public String main(Model model) {
        Advice advice = mainService.findByAll().get(0);
        model.addAttribute("adviceModel", advice);

        return "index";
    }

}
