package com.fmi.nada.diary;

import com.fmi.nada.user.BlockListDto;
import com.fmi.nada.user.Likes;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.Sympathy;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
        model.addAttribute("sympathyBeen", sympathy);

        Diary diary = diaryService.getDiaryDetail(diaryIdx);
        Diary readDiaryBean = diaryService.viewAdd(diary);
        model.addAttribute("readDiaryBean", readDiaryBean);

        getCommentList(diaryIdx, model, member);

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

        analyzedService.resisterAnalyze(
                diary.getDiaryIdx(),
                diary.getDiaryAnalyze(),
                diaryDTO.getAnalyzeScore());

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

        getCommentList(diaryIdx, model, member);

        // jQuery를 사용하여 AJAX 요청을 보내고, 요청이 성공하면
        // "/diary/read" URL에서 가져온 데이터 중 "tbody" 요소를 반환
        return "/diary/read :: tbody";
    }
    @RequestMapping(value = "/comment_modify", method = RequestMethod.POST)
    public String commentModify(@RequestParam("diaryIdx") Long diaryIdx,
                                @RequestParam("commentIdx") Long commentIdx,
                               @RequestParam("commentModifyInput") String commentModifyInput,
                               Authentication authentication,
                               Model model) {

        Member member = (Member) authentication.getPrincipal();

        Comment comment = commentService.findByDiaryIdxAndCommentIdxAndMemberIdx(diaryIdx, commentIdx, member.getMemberIdx());
        comment.setCommentContent(commentModifyInput);

        commentService.resisterComment(comment);

        getCommentList(diaryIdx, model, member);

        // jQuery를 사용하여 AJAX 요청을 보내고, 요청이 성공하면
        // "/diary/read" URL에서 가져온 데이터 중 "tbody" 요소를 반환
        return "/diary/read :: tbody";
    }

    @RequestMapping(value = "/comment_delete", method = RequestMethod.POST)
    public String commentDelete(@RequestParam("diaryIdx") Long diaryIdx,
                                @RequestParam("commentIdx") Long commentIdx,
                                Authentication authentication,
                                Model model) {
        Member member = (Member) authentication.getPrincipal();
        commentService.deleteByCommentIdx(commentIdx);
        getCommentList(diaryIdx, model, member);

        return "/diary/read :: tbody";
    }

    // 다이어리 수정 페이지
    @GetMapping("/modify")
    public String modifyDiary(
            @RequestParam("diaryIdx") Long diaryIdx,
            @RequestParam("page") int page,
            @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO,
            Authentication authentication,
            Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Diary diary = diaryService.findByDiaryIdx(diaryIdx);
        Integer score = analyzedService.findByDiaryIdx(diaryIdx).getAnalyzeScore();

        diaryDTO.setDiarySubject(diary.getDiarySubject());
        diaryDTO.setDiaryWriter(diary.getDiaryWriter());
        diaryDTO.setDiaryContent(diary.getDiaryContent());
        diaryDTO.setDiaryKeywords(diary.getDiaryKeywords());
        diaryDTO.setDiaryAnalyze(diary.getDiaryAnalyze());
        diaryDTO.setAnalyzeScore(score);
        diaryDTO.setDiaryPublicable(diary.getDiaryPublicable());
        diaryDTO.setDiaryAnalyzePublicable(diary.getDiaryAnalyzePublicable());
        model.addAttribute("diaryModifyBean", diaryDTO);

        model.addAttribute("diaryIdx", diaryIdx);
        model.addAttribute("page", page);

        return "/diary/modify";
    }


    // 다이어리 수정 로직
    @PutMapping("/modify_pro")
    public String modifyDiary_pro(
            @RequestParam("diaryIdx") Long diaryIdx,
            @RequestParam("page") int page,
            @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {
        if (bindingResult.hasErrors()) return "diary/modify";

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Diary diary = diaryService.findByDiaryIdx(diaryIdx);
        diary.setDiarySubject(diaryDTO.getDiarySubject());
        diary.setDiaryContent(diaryDTO.getDiaryContent());
        diary.setDiaryKeywords(diaryDTO.getDiaryKeywords());
        diary.setDiaryAnalyze(diaryDTO.getDiaryAnalyze());
        diary.setDiaryPublicable(diaryDTO.getDiaryPublicable());
        diary.setDiaryAnalyzePublicable(diaryDTO.getDiaryAnalyzePublicable());

        Analyzed analyzed = analyzedService.findByDiaryIdx(diaryIdx);
        analyzed.setAnalyzeScore(diaryDTO.getAnalyzeScore());

        insertKeywords(diaryDTO);

        diaryService.modifyDiary(diary);
        analyzedService.analyzedModify(analyzed);

        return "redirect:/diary/read/" + diary.getDiaryIdx() + "?page=" + page;
    }

    // 다이어리 삭제 로직
    @DeleteMapping("/delete")
    public String deleteDiary(@RequestParam("deleteDiaryIdx") Long diaryIdx) {
        diaryService.deleteDiary(diaryIdx);
        return "redirect:/diary?page=" + 1;
    }

    // 다이어리 공감
    @PostMapping("/sympathy")
    public String addSympathy(
            DiarySympathyDto diarySympathyDto,
            Authentication authentication,
            Model model
    ) {
        Member member = (Member) authentication.getPrincipal();
        diarySympathyDto.setMemberIdx(member.getMemberIdx());

        diaryService.addSympathy(diarySympathyDto);

        Sympathy sympathyBeen = diaryService.checkSympathy(diarySympathyDto);
        model.addAttribute("sympathyBeen", sympathyBeen);

        Diary readDiaryBean = diaryService.findByDiaryIdx(diarySympathyDto.getDiaryIdx());
        model.addAttribute("readDiaryBean", readDiaryBean);

        return "/diary/read :: #sympathyBtn";
    }

    // 다이어리 공감 취소
    @DeleteMapping("/sympathy")
    public String delSympathy(
            DiarySympathyDto diarySympathyDto,
            Authentication authentication,
            Model model) {
        Member member = (Member) authentication.getPrincipal();
        diarySympathyDto.setMemberIdx(member.getMemberIdx());

        diaryService.delSympathy(diarySympathyDto);

        Sympathy sympathyBeen = diaryService.checkSympathy(diarySympathyDto);
        model.addAttribute("sympathyBeen", sympathyBeen);

        Diary readDiaryBean = diaryService.findByDiaryIdx(diarySympathyDto.getDiaryIdx());
        model.addAttribute("readDiaryBean", readDiaryBean);

        return "/diary/read :: #sympathyBtn";
    }

    // 댓글 좋아요
    @PostMapping("/commentLike")
    public String addCommentLike(
            CommentLikeDto commentLikeDto,
            Authentication authentication,
            Model model) {
        Member member = (Member) authentication.getPrincipal();
        commentLikeDto.setMemberIdx(member.getMemberIdx());

        diaryService.addCommentLike(commentLikeDto);

        getCommentList(commentLikeDto.getDiaryIdx(), model, member);

        return "/diary/read :: tbody";
    }

    @DeleteMapping("/commentLike")
    public String delCommentLike(CommentLikeDto commentLikeDto,
                                 Authentication authentication,
                                 Model model) {
        System.out.println("게시글 좋아요 취소 돌입");
        Member member = (Member) authentication.getPrincipal();
        commentLikeDto.setMemberIdx(member.getMemberIdx());

        diaryService.delCommentLike(commentLikeDto);

        getCommentList(commentLikeDto.getDiaryIdx(), model, member);

        return "/diary/read :: tbody";
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

    private void getCommentList(Long diaryIdx, Model model, Member member) {
        List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
        model.addAttribute("commentList", commentList);

        List<Boolean> commentIsLikes = new ArrayList<>();
        for (Comment c : commentList) {
            Likes likes = diaryService.checkCommentLikeWhenRead(member.getMemberIdx(), c.getCommentIdx());
            if (likes != null)
                commentIsLikes.add(true);
            else
                commentIsLikes.add(false);
        }
        model.addAttribute("commentIsLikes", commentIsLikes);
    }

}
