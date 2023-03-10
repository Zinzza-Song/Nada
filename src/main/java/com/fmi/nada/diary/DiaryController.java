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
 * */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diary/")
public class DiaryController {

    private final DiaryService diaryService;
    private final DiaryRepository diaryRepository;

    //좋아요를 위한 Like 가져와야 함.

    @GetMapping
    public String DiaryMain(Model model) {
        List<Diary> diaryList = diaryService.getDiaryList();
        model.addAttribute("allDiaryList", diaryList);
        return "index";
    }

    @GetMapping("write")
    public String DiaryWrite(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO) {
            return "diary/write";
    }

    @PostMapping("write_pro")
    public String DiaryWrite_pro(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model){
        if(bindingResult.hasErrors())
            return "diary/wirte";

        Member member = (Member)authentication.getPrincipal();

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

        return "redirect:/read/" + 1;

    }



}
