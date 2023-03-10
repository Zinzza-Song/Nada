package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary/")
public class DiaryController {

    private final DiaryService diaryService;
    @GetMapping
    public String main() {
        List<Diary> diary =
        return "index";
    }




}
