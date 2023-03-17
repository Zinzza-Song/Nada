package com.fmi.nada.diary;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Diary Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    private final KeywordService keywordService;

    private final CommentService commentService;

    private final AnalyzedService analyzedService;
    //좋아요를 위한 Like 가져와야 함.

    //다이어리 게시판 페이지
    @GetMapping
    public String DiaryMain(@PageableDefault Pageable pageable,
                            Model model) {
        model.addAttribute("allDiaryList", diaryService.getDiaryList(pageable));
        return "diary/index";
    }

    @GetMapping("search")
    public String DiarySearch(@PageableDefault Pageable pageable,
                              @RequestParam("type") String type,
                              @RequestParam("keyword") String keyword,
                              Model model) {
        if (type.isBlank() && keyword.isBlank()) {
            model.addAttribute("allDiaryList", diaryService.getDiaryList(pageable));
        } else if (type.equals("content")) {
            model.addAttribute("allDiaryList", diaryService.findAllByDiaryContentContaining(keyword, pageable));
        } else if (type.equals("writer")) {
            model.addAttribute("allDiaryList", diaryService.findAllByDiaryWriterContaining(keyword, pageable));
        } else if (type.equals("keyword")) {
            model.addAttribute("allDiaryList", diaryService.findAllByDiaryKeywordsContaining(keyword, pageable));
        }
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "diary/index";
    }

    // 다이어리 상세 페이지
    @GetMapping("read")
    public String readDiary(@RequestParam(value = "diaryIdx", required = false) Long diaryIdx,
                            Authentication authentication,
                            Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Diary diary = diaryService.getDiaryDetail(diaryIdx);
        model.addAttribute("readDiaryBean", diary);

        List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
        model.addAttribute("commentList", commentList);

        return "diary/read";
    }

    // 다이어리 작성 페이지
    @GetMapping("/write")
    public String DiaryWrite(@ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO, Authentication authentication, Model model) {

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);
        diaryDTO.setDiaryWriter(member.getMemberNickname());

        return "diary/write";
    }

    @PostMapping("write_pro")
    public String DiaryWrite_pro(
            @Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
            BindingResult bindingResult,
            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            for (int i = 0; i < bindingResult.getAllErrors().size(); ++i)
                System.out.println(bindingResult.getAllErrors().get(i));
            return "diary/write";
        }

        Member member = (Member) authentication.getPrincipal();

        if (diaryDTO.getDiaryKeywords() == null)
            System.out.println("널값뜸");

        System.out.println(diaryDTO.getDiaryKeywords());

        Diary diary = diaryService.registerDiary(
                member.getMemberIdx(),
                diaryDTO.getDiarySubject(),
                diaryDTO.getDiaryWriter(),
                diaryDTO.getDiaryContent(),
                diaryDTO.getDiaryKeywords(),
                diaryDTO.getDiaryAnalyze(),
                diaryDTO.getDiaryPublicable(),
                diaryDTO.getDiaryAnalyzePublicable());

        analyzedService.resisterAnalyze(diary.getDiaryIdx(), diary.getDiaryAnalyze(), Integer.parseInt(diaryDTO.getAnalyzeScore()));

        insertKeywords(diaryDTO);

        return "redirect:/read/" + diary.getDiaryIdx();
    }


    // 다이어리 공감에 대한 ajax
    @GetMapping("sympathy/{diaryIdx}")
    @ResponseBody
    public String sympathyAjax(@PathVariable("diaryIdx") Long diaryIdx) {
        return null;
    }

    // 댓글 좋아요에 대한 ajax
    @GetMapping("comment_like/{diaryIdx}")
    @ResponseBody
    public String likeAjax(@PathVariable("diaryIdx") Long diaryIdx) {
        return null;
    }

    // 다이어리 수정 페이지
    @GetMapping("modify/{diaryIdx}")
    public String modifyDiary(@PathVariable("diaryIdx") Long diaryIdx, @RequestParam("pageCnt") int pageCnt, @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO, Authentication authentication, Model model) {

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;
    }


    // 다이어리 수정 로직
    @PutMapping("modify_pro/{diaryIdx}")
    public String modifyDiary_pro(@PathVariable("diaryIdx") Long diaryIdx, @RequestParam("pageCnt") int pageCnt, @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO, BindingResult bindingResult, Authentication authentication, Model model) {
        if (bindingResult.hasErrors()) return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Diary diary = diaryService.findByDiaryIdx(diaryIdx);
        diary.setDiarySubject(diaryDTO.getDiarySubject());
        diary.setDiaryContent(diaryDTO.getDiaryContent());
        diary.setDiaryKeywords(diaryDTO.getDiaryKeywords());
        diary.setDiaryAnalyze(diaryDTO.getDiaryAnalyze());
        diary.setDiaryPublicable(diaryDTO.getDiaryPublicable());
        diary.setDiaryAnalyzePublicable(diaryDTO.getDiaryAnalyzePublicable());

        insertKeywords(diaryDTO);
        diaryService.modifyDiary(diary);

        return "redirect:/read/" + diary.getDiaryIdx() + "?pageCnt=" + pageCnt;
    }

    // 다이어리 삭제 로직
    @DeleteMapping("delete/{diaryIdx}")
    public String deleteDiary(@PathVariable("diaryIdx") Long diaryIdx) {
        diaryService.deleteDiary(diaryIdx);
        return "redirect:index?pageNum=" + 1;
    }

    private void insertKeywords(DiaryDTO diaryDTO) {
        String keywordArr[] = diaryDTO.getDiaryKeywords().split(",");
        for (int i = 0; i < keywordArr.length; i++) {
            if (keywordArr[i] != null) {
                Keyword keyword = keywordService.findByKeywordName(keywordArr[i]);

                if (keyword != null) {
                    Integer a = keyword.getKeywordCnt();
                    System.out.println(a);
                    Integer b = a + 1;
                    keyword.setKeywordCnt(b);
                    keywordService.register(keyword);
                } else {
                    keywordService.register(new Keyword(keywordArr[i]));
                }
            }
        }
    }

}
