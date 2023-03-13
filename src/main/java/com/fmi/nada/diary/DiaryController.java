package com.fmi.nada.diary;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
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
 * 다이어리 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diary/")
public class DiaryController {

    private final DiaryService diaryService;
    private final KeywordService keywordService;
    private final AnalyzedService analyzedService;
    private final CommentService commentService;

    //좋아요를 위한 Like 가져와야 함.

    //다이어리 게시판 페이지
    @GetMapping
    public String DiaryMain(Model model) {
        List<Diary> diaryList = diaryService.getDiaryList();
        model.addAttribute("allDiaryList", diaryList);
        return "diary/index";
    }

    //다이어리 상세 페이지
    @GetMapping("read/{diaryIdx}")
    public String DiaryRead(@PathVariable("diartIdx") Long diaryIdx,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Model model) {

        Diary diary = diaryService.getDiaryDetail(diaryIdx);
        viewCountValidation(diary, request, response);
        model.addAttribute("diaryBean", diary);
        List<Comment> commentList = commentService.getCommentListByDiaryIndex(diaryIdx);
        model.addAttribute("commentList", commentList);
        return "diary/read/" + diary.getDiaryIdx();
    }

    //다이어리 작성 페이지
    @GetMapping("write")
    public String DiaryWrite(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                             Authentication authentication,
                             Model model) {

        Member member = (Member) authentication.getPrincipal();

        model.addAttribute("member", member);

        return "diary/write";
    }

    //다이어리 작성
    @PostMapping("write_pro")
    public String DiaryWrite_pro(@Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model) {
        if (bindingResult.hasErrors())
            return "diary/wirte";

        Member member = (Member) authentication.getPrincipal();

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

        //작성 완료시 상세 페이지로 이동할 데이터 (방금 작성한 일기 데이터)
        Diary diary = diaryService.findByDiary_subject(diaryDTO.getDiarySubject());

        //일기 분석 정보 저장
        analyzedService.resiterAnalyze(diary.getDiaryIdx(), diaryDTO.getDiaryAnalyze(), diaryDTO.getAnalyzeScore());

        //키워드 저장 로직
        String keywordArr[] = diaryDTO.getDiaryKeywords().split(",");
        for (int i = 0; i < keywordArr.length; i++) {
            if (keywordArr[i] != null) {
                Keyword keyword = keywordService.findByKeyword_name(keywordArr[i]);

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



    // 조회수 증가에 대한 중복 조회 방지 메서드 ( 24시간 기준으로 여러번 방문해도 조회수는 1 증가 )
    private void viewCountValidation(Diary diary, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        // request에 쿠키들이 있을 때
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            // postView 쿠키가 있을 때
            if (cookies[i].getName().equals("noticeView")) {
                // cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + diary.getDiaryIdx() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    diary.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + diary.getDiaryIdx() + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 noticeView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            diary.addViewCount();
            cookie = new Cookie("noticeView", "[" + diary.getDiaryIdx() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
