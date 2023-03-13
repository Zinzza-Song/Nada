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
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    private final KeywordService keywordService;

    private final CommentService commentService;

    private final AnalyzedService analyzedService;
    //좋아요를 위한 Like 가져와야 함.

    //다이어리 게시판 페이지
    @GetMapping("/")
    public String DiaryMain(@RequestParam("page") Integer page,
                            Model model) {
        List<Diary> diaryList = diaryService.getDiaryList();
        model.addAttribute("allDiaryList", diaryList);
        return "diary/index";
    }

    
    // 다이어리 상세 페이지
    @GetMapping("read/{diaryIdx}")
    public String readDiary(@PathVariable("diaryIdx") Long diaryIdx,
                            @RequestParam("pageNum") int pageNum,
                            Authentication authentication,
                            Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Diary diary = diaryService.getDiaryDetail(diaryIdx);
        model.addAttribute("diaryBean", diary);

        List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
        model.addAttribute("commentList", commentList);

        return "diary/read/" + diaryIdx + "?pageNum=" + pageNum;
    }

    // 다이어리 작성 페이지
    @GetMapping("write/{memberIdx}")
    public String DiaryWrite(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                             @PathVariable("memberIdx") Long memberIdx,
                             Authentication authentication,
                             Model model) {
        
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        return "diary/write/" + memberIdx;
    }

    @PostMapping("write_pro")
    public String DiaryWrite_pro(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model) {
        if (bindingResult.hasErrors())
            return "diary/wirte";

        Member member = (Member) authentication.getPrincipal();
        diaryDTO.setDiaryWriter(member.getMemberNickname());

        diaryService.registerDiary(
                member.getMemberIdx(),
                diaryDTO.getDiarySubject(),
                diaryDTO.getDiaryWriter(),
                diaryDTO.getDiaryContent(),
                diaryDTO.getDiaryKeywords(),
                diaryDTO.getDiaryAnalyze(),
                diaryDTO.getDiaryPublicable(),
                diaryDTO.getDiaryAnalyzePublicable()
        );

        Diary diary = diaryService.findByDiary_subject(diaryDTO.getDiarySubject());
        analyzedService.resiterAnalyze(diary.getDiaryIdx(), diary.getDiaryAnalyze(), diaryDTO.getAnalyzeScore());

        String keywordArr[] = diaryDTO.getDiaryKeywords().split(",");
        for (int i = 0; i < keywordArr.length; i++) {
            if (keywordArr[i] != null) {
                Keyword keyword = keywordService.findByKeywordName(keywordArr[i]);

                if (keyword != null) {
                    keyword.setKeywordCnt(keyword.getKeywordCnt() + 1);
                    keywordService.register(keyword);
                } else {
                    keyword.setKeywordName(keywordArr[i]);
                    keywordService.register(keyword);
                }
            }
        }
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
    public String modifyDiary(@PathVariable("diaryIdx") Long diaryIdx,
                              @RequestParam("pageCnt") int pageCnt,
                              @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO,
                              Authentication authentication,
                              Model model) {

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;
    }


    // 다이어리 수정 로직
    @PutMapping("modify_pro/{diaryIdx}")
    public String modifyDiary_pro(@PathVariable("diaryIdx") Long diaryIdx,
                                  @RequestParam("pageCnt") int pageCnt,
                                  @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO,
                                  BindingResult bindingResult,
                                  Authentication authentication,
                                  Model model) {
        if (bindingResult.hasErrors())
            return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        diaryService.registerDiary(
                member.getMemberIdx(),
                diaryDTO.getDiarySubject(),
                diaryDTO.getDiaryWriter(),
                diaryDTO.getDiaryContent(),
                diaryDTO.getDiaryKeywords(),
                diaryDTO.getDiaryAnalyze(),
                diaryDTO.getDiaryPublicable(),
                diaryDTO.getDiaryAnalyzePublicable()
        );

        Diary diary = diaryService.findByDiary_subject(diaryDTO.getDiarySubject());

        String keywordArr[] = diaryDTO.getDiaryKeywords().split(",");
        for (int i = 0; i < keywordArr.length; i++) {
            if (keywordArr[i] != null) {
                Keyword keyword = keywordService.findByKeywordName(keywordArr[i]);

                if (keyword != null) {
                    keyword.setKeywordCnt(keyword.getKeywordCnt() + 1);
                    keywordService.register(keyword);
                } else {
                    keyword.setKeywordName(keywordArr[i]);
                    keywordService.register(keyword);
                }
            }
        }
        return "redirect:/read/" + diary.getDiaryIdx();
    }

    // 다이어리 삭제 로직
    @DeleteMapping("delete/{diaryIdx}")
    public String deleteDiary(@PathVariable("diaryIdx") Long diaryIdx){
        diaryService.deleteDiary(diaryIdx);
        return "redirect:index?pageNum=" + 1;
    }

}
