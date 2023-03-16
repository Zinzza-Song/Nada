package com.fmi.nada.board.qna;


import com.fmi.nada.board.notice.Notice;
import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * QnA Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/qna")
public class QnaController {
    //서비스 주입
    private final QnaService qnaService;

    //QNA 메인
    @GetMapping
    public String index(Model mo) {
        List<Qna> li = qnaService.getList();
        mo.addAttribute("list", li);
        return "board/QNA/index";
    }

    //QNA 작성페이지
    @GetMapping("/write")
    public String qnaWrite(@ModelAttribute("addQnaBean") QnaDto qnaDto,
                           Authentication authentication, Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("memberLoginBean",member);
        return "board/QNA/write";
    }

    //QNA 작성 프로세스 실행 이후 리다이렉트
    @PostMapping("/write_pro")
    public String qnaWrite(@Valid @ModelAttribute("addQnaBean") QnaDto qnaDto
            , Authentication authentication){
        //유저 정보를 로그인된 것을 기준으로 불러와서 member_idx를 불러옴
        Member member = (Member) authentication.getPrincipal();
        Long memberIdx = member.getMemberIdx();
        //문의사항 작성 시 필요한 내용은 제목과 내용, 파일도 있네 잠깐만 추가하자
        Qna qna = new Qna();
        qna.setMemberIdx(memberIdx);
        qna.setQnaSubject(qnaDto.getQnaSubject());
        qna.setQnaContent(qnaDto.getQnaContent());
        qna.setQnaFile(qnaDto.getQnaFile());
        qnaService.writeQna(qna);

        return "redirect:read?qnaIdx=" + qna.getQnaIdx();

    }

    /*@PutMapping
    public String upViews(){

    }*/

    //QNA 상세페이지
    @GetMapping("/read")
    public String read(@ModelAttribute("qnaBean") QnaDto qnaDto,
                       @ModelAttribute("qnaAdminBean") QnaAnswerDto qnaAnswerDto,
                       @RequestParam("qnaIdx") Long qnaIdx, Model mo) {
        Qna qna = qnaService.get(qnaIdx);
        mo.addAttribute("read", qna);
        return "board/QNA/read";
    }

    //QNA 답변페이지
    @PutMapping("/answer")
    @ResponseBody
    public void answerQna(@ModelAttribute("qnaAdminBean") QnaAnswerDto qnaAnswerDto,
                          Long qnaIdx,String qnaAnswer) {
        Qna qna = qnaService.get(qnaIdx);
        qna.setQnaAnswer(qnaAnswer);
        qna.setQnaIsanswered(true);
        qnaService.answerQna(qna);
    }

    //QNA 삭제
    @DeleteMapping("/delete")
    public void delete(@RequestParam("qna_idx") Long qna_idx) {
        qnaService.deleteQna(qna_idx);
    }


}
