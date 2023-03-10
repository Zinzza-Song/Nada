package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * 다이어리 컨트롤러
 * */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diary/")
public class DiaryController {

    private final DiaryService diaryService;
    @GetMapping
    public String main() {
        return "index";
    }




}
