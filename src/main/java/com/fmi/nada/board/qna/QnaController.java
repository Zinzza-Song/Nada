package com.fmi.nada.board.qna;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public String qnaWrite() {
        return "board/QNA/write";
    }

    //QNA 작성 프로세스 실행 이후 리다이렉트
    @PostMapping("/write_pro")
    public String qnaWrite(@Valid @ModelAttribute("writeQnaBean") QnaDto qnaDto) {
//        user 어쩌고 시큐리티 어쩌고 할곳
        Qna qna = new Qna();
        qna.setQnaSubject(qnaDto.getQnaSubject());
        qna.setQnaContent(qnaDto.getQnaContent());
        qnaService.writeQna(qna);
        return "redirect:read";
    }

    /*@PutMapping
    public String upViews(){

    }*/

    //QNA 상세페이지
    @GetMapping("/read")
    public String read(@RequestParam("qna_idx") Long qna_idx, Model mo) {
        Qna qna = qnaService.get(qna_idx);
        mo.addAttribute("read", qna);
        return "board/QNA/read";
    }

    //QNA 답변페이지
    @PutMapping("/answer")
    public void answerQna(@ModelAttribute QnaDto qnaDto, @RequestParam("qna_idx") Long qna_idx) {
        Qna qna = qnaService.get(qna_idx);
        qna.setQnaAnswer(qnaDto.getQnaAnswer());
        qna.setQnaIsanswered(true);
        qnaService.answerQna(qna);
    }

    //QNA 삭제
    @DeleteMapping("/delete")
    public void delete(@RequestParam("qna_idx") Long qna_idx) {
        qnaService.deleteQna(qna_idx);
    }


}
