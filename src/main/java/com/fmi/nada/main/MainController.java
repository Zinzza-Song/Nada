package com.fmi.nada.main;

import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import com.fmi.nada.diary.Keyword;
import com.fmi.nada.diary.KeywordService;
import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final KeywordService keywordService;
    private final DiaryService diaryService;

    /**
     * 오늘의 조언 랜덤 조회
     *
     * @return 메인 페이지 view("/index.html")
     */
    @GetMapping("/")
    public String main(Model model) {

        Advice advice = mainService.findByAll().get(0);
        model.addAttribute("adviceModel", advice);

        List<Keyword> keywordList = keywordService.findTop5ByOrderByKeywordCntDesc();
        model.addAttribute("keywordList", keywordList);

        List<Diary> recentTop5DiaryList = diaryService.findTop5ByOrderByDiaryDateDesc();
        model.addAttribute("recentTop5DiaryList", recentTop5DiaryList);

        List<Diary> mostSympathyTop5DiaryList = diaryService.findTop5ByOrderByDiarySympathyCntDesc();
        model.addAttribute("mostSympathyTop5DiaryList", mostSympathyTop5DiaryList);

        return "index";
    }

}
