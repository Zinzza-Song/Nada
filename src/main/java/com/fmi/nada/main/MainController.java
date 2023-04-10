package com.fmi.nada.main;

import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import com.fmi.nada.diary.Keyword;
import com.fmi.nada.diary.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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

        List<Diary> recentTop5DiaryList = diaryService.findTop8ByOrderByDiaryDateDesc();
        model.addAttribute("recentTop8DiaryList", recentTop5DiaryList);

        List<Diary> mostSympathyTop5DiaryList = diaryService.findTop8ByOrderByDiarySympathyCntDesc();
        model.addAttribute("mostSympathyTop8DiaryList", mostSympathyTop5DiaryList);

        return "index";
    }

    @GetMapping("/download")
    public StreamingResponseBody stream(HttpServletRequest req) throws Exception {
        File file = new File("src/main/resources/static/videos/main_bannerVideo2.mp4");
        final InputStream is = new FileInputStream(file);
        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }

    @GetMapping("/denied")
    public String denied() {
        return "denied";
    }

}
