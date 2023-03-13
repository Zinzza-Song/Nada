package com.fmi.nada.diary;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 다이어리 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diary/")
public class DiaryController {

    private final DiaryService diaryService;
    private final KeywordService keywordService;

    //좋아요를 위한 Like 가져와야 함.

    @GetMapping
    public String DiaryMain(Model model) {
        List<Diary> diaryList = diaryService.getDiaryList();
        model.addAttribute("allDiaryList", diaryList);
        return "diary/index";
    }

    @GetMapping("write")
    public String DiaryWrite(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO, Authentication authentication, Model model) {
        Member member = (Member) authentication.getPrincipal();

        model.addAttribute("member", member);

        return "diary/write";
    }

    @PostMapping("write_pro")
    public String DiaryWrite_pro(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model) {
        if (bindingResult.hasErrors())
            return "diary/wirte";

        Member member = (Member) authentication.getPrincipal();

        diaryService.registerDiary(
                member.getMember_idx(),
                diaryDTO.getDiary_subject(),
                diaryDTO.getDiary_writer(),
                diaryDTO.getDiary_content(),
                diaryDTO.getDiary_keywords(),
                diaryDTO.getDiary_analyze(),
                diaryDTO.getDiary_publicable(),
                diaryDTO.getDiary_analyze_publicable()
        );

        Diary diary = diaryService.findByDiary_subject(diaryDTO.getDiary_subject());

        String keywordArr[] = diaryDTO.getDiary_keywords().split(",");
        for (int i = 0; i < keywordArr.length; i++) {
            if (keywordArr[i] != null) {
                Keyword keyword = keywordService.findByKeyword_name(keywordArr[i]);

                if (keyword != null) {
                    keyword.setKeyword_cnt(keyword.getKeyword_cnt() + 1);
                    keywordService.register(keyword);
                } else {
                    keyword.setKeyword_name(keywordArr[i]);
                    keywordService.register(keyword);
                }
            }
        }

        return "redirect:/read/" + diary.getDiary_idx();
    }


}
