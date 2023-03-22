package com.fmi.nada.diary;

import com.fmi.nada.user.BlockListDto;
import com.fmi.nada.user.Likes;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.Sympathy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {

        Page<Diary> allDiaryList = null;

        if (keyword == null) {
            allDiaryList = diaryService.getDiaryList(pageable);
        } else if (type.equals("content")) {
            allDiaryList = diaryService.findAllByDiaryContentContaining(keyword, pageable);
        } else if (type.equals("writer")) {
            allDiaryList = diaryService.findAllByDiaryWriterContaining(keyword, pageable);
        } else if (type.equals("keyword")) {
            allDiaryList = diaryService.findAllByDiaryKeywordsContaining(keyword, pageable);
        }

        model.addAttribute("allDiaryList", allDiaryList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "diary/index";
    }

    // 다이어리 상세 페이지
    @GetMapping("/read/{diaryIdx}")
    public String readDiary(@PathVariable("diaryIdx") Long diaryIdx,
                            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                            Authentication authentication,
                            Model model,
                            @ModelAttribute("blockListDto") BlockListDto blockListDto) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Sympathy sympathy = diaryService.checkSympathyDiary(member.getMemberIdx(), diaryIdx);
        if (sympathy != null)
            model.addAttribute("sympathyBean", sympathy);

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

    @PostMapping("/write_pro")
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

        Diary diary = diaryService.registerDiary(
                member.getMemberIdx(),
                diaryDTO.getDiarySubject(),
                diaryDTO.getDiaryWriter(),
                diaryDTO.getDiaryContent(),
                diaryDTO.getDiaryKeywords(),
                diaryDTO.getDiaryAnalyze(),
                diaryDTO.getDiaryPublicable(),
                diaryDTO.getDiaryAnalyzePublicable());

        analyzedService.resisterAnalyze(diary, diary.getDiaryAnalyze(), Integer.parseInt(diaryDTO.getAnalyzeScore()));

        insertKeywords(diaryDTO);

        return "redirect:/diary/read/" + diary.getDiaryIdx();
    }

    @RequestMapping(value = "/comment_write", method = RequestMethod.POST)
    public String commentWrite(@RequestParam("diaryIdx") Long diaryIdx,
                               @RequestParam("commentInput") String commentInput,
                               Authentication authentication,
                               Model model) {

        Member member = (Member) authentication.getPrincipal();
        Comment comment = new Comment(member.getMemberIdx(),
                diaryIdx,
                commentInput,
                member.getMemberNickname(),
                member.getUsername()
        );
        comment.setCommentLikeCnt(0);

        commentService.resisterComment(comment);

        List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
        model.addAttribute("commentList", commentList);

        // jQuery를 사용하여 AJAX 요청을 보내고, 요청이 성공하면
        // "/diary/read" URL에서 가져온 데이터 중 "tbody" 요소를 반환
        return "/diary/read :: tbody";
    }

    // 다이어리 수정 페이지
    @GetMapping("/modify/{diaryIdx}")
    public String modifyDiary(
            @PathVariable("diaryIdx") Long diaryIdx,
            @RequestParam("pageCnt") int pageCnt,
            @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO,
            Authentication authentication,
            Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;
    }


    // 다이어리 수정 로직
    @PutMapping("/modify_pro/{diaryIdx}")
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
    @DeleteMapping("/delete/{diaryIdx}")
    public String deleteDiary(@PathVariable("diaryIdx") Long diaryIdx) {
        diaryService.deleteDiary(diaryIdx);
        return "redirect:index?pageNum=" + 1;
    }

    // 다이어리 공감
    @PostMapping("/sympathy")
    @ResponseBody
    public String addSympathy(DiarySympathyDto diarySympathyDto) {
        Sympathy sympathy = diaryService.checkSympathy(diarySympathyDto);
        if (sympathy != null)
            return "fail";

        diaryService.addSympathy(diarySympathyDto);

        return "ok";
    }

    // 다이어리 공감 취소
    @DeleteMapping("/sympathy")
    @ResponseBody
    public String delSympathy(DiarySympathyDto diarySympathyDto) {
        Sympathy sympathy = diaryService.checkSympathy(diarySympathyDto);
        if (sympathy == null)
            return "fail";

        diaryService.delSympathy(diarySympathyDto);

        return "ok";
    }

    // 댓글 좋아요
    @PostMapping("/commentLike")
    @ResponseBody
    public String addCommentLike(CommentLikeDto commentLikeDto) {
        Likes likes = diaryService.checkCommentLike(commentLikeDto);
        if (likes != null)
            return "fail";

        diaryService.addCommentLike(commentLikeDto);

        return "ok";
    }

    @DeleteMapping("/commentLike")
    @ResponseBody
    public String delCommentLike(CommentLikeDto commentLikeDto) {
        Likes likes = diaryService.checkCommentLike(commentLikeDto);
        if (likes == null)
            return "fail";

        diaryService.delCommentLike(commentLikeDto);

        return "ok";
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
