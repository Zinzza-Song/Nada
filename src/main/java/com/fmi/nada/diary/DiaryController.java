package com.fmi.nada.diary;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @GetMapping
    public String DiaryMain(@PageableDefault(page=0, size=3, sort="diaryDate", direction= Sort.Direction.DESC) Pageable pageable,
                            Model model) {
        /**
         * 페이징 처리
         *
         클라이언트에서 전달받은 pageCnt와 실제 접근 페이지는 다르다.
         Page 객체는 0부터 시작하기 때문에 실제 접근 페이지는 pageNo - 1 해주어야 한다.
         */
        Page<Diary> diaryList = diaryService.getDiaryList(pageable);
        int nowPage = diaryList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage, 1);
        int endPage = Math.min(nowPage+9, diaryList.getTotalPages());

        model.addAttribute("allDiaryList", diaryList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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
    @GetMapping("/write")
    public String DiaryWrite(@ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                             Authentication authentication,
                             Model model) {

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
            return "diary/write";

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
        analyzedService.resisterAnalyze(diary.getDiaryIdx(), diary.getDiaryAnalyze(), diaryDTO.getAnalyzeScore());

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
                    keyword.setKeywordCnt(keyword.getKeywordCnt() + 1);
                    keywordService.register(keyword);
                } else {
                    keyword.setKeywordName(keywordArr[i]);
                    keywordService.register(keyword);
                }
            }
        }
    }

}
