package com.fmi.nada.board.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/QNA/")
public class qnaController {
    //서비스 주입
    private final qnaServiceImpl qnaService;

    //QNA 메인
    @GetMapping("/")
    public String index(Model mo){
        List<Qna> li = qnaService.getList();
        mo.addAttribute("list",li);
        return "QNA/index";
    }

    //QNA 작성페이지
    @GetMapping("/write")
    public String qnaWrite(){
        return "QNA/write";
    }

    //QNA 작성 프로세스 실행 이후 리다이렉트
    @PostMapping("/write_pro")
    public String qnaWrite(Qna qna){
        qnaService.writeQna(qna);
        return "redirect:read";
    }

    /*@PutMapping
    public String upViews(){

    }*/

    //QNA 상세페이지
    @GetMapping("/read")
    public String read(@RequestParam("qna_idx") Long qna_idx, Model mo){
        Qna qna = qnaService.get(qna_idx);
        mo.addAttribute("read",qna);
        return "QNA/read";
    }

    //QNA 답변페이지
    @PutMapping("/answer")
    public void answerQna(@ModelAttribute QnaDto qnaDto){
        Qna qna = qnaService.get(qnaDto.getQna_idx());
        qna.setQna_answer(qnaDto.getQna_answer());
        qna.setQna_isanswered(true);
        qnaService.answerQna(qna);
    }

    //QNA 삭제
    @DeleteMapping("/delete")
    public void delete(@RequestParam("qna_idx") Long qna_idx){
        qnaService.deleteQna(qna_idx);
    }



}
