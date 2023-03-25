package com.fmi.nada.board.qna;


import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

/**
 * QnA Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/QNA")
public class QnaController {
    //서비스 주입
    private final QnaService qnaService;

    //QNA 메인
    @GetMapping
    public String index(@PageableDefault Pageable pageable,
                        Authentication authentication,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {
        Member member = (Member) authentication.getPrincipal();
        Page<Qna> li = null;
        if (keyword == null) {
            li = qnaService.findAllByOrderByQnaDateDesc(pageable);
        } else if (type.equals("qnaSubject")) {
            li = qnaService.findAllByQnaSubjectContaining(keyword, pageable);
        } else if (type.equals("qnaWriter")) {
            li = qnaService.findAllByQnaWriterContaining(keyword, pageable);
        }
        model.addAttribute("allQnaList", li);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("member", member);
        return "board/QNA/index";
    }

    //QNA 작성페이지
    @GetMapping("/write")
    public String qnaWrite(@ModelAttribute("addQnaBean") QnaDto qnaDto,
                           Authentication authentication, Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("memberLoginBean", member);
        return "board/QNA/write";
    }

    //QNA 작성 프로세스 실행 이후 리다이렉트
    @PostMapping("/write_pro")
    public String qnaWrite(@Valid @ModelAttribute("addQnaBean") QnaDto qnaDto,
                           BindingResult result,
                           Authentication authentication,
                           @RequestParam(required = false) MultipartFile file, Model model) throws Exception {
        //유저 정보를 로그인된 것을 기준으로 불러와서 member_idx를 불러옴

        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("memberLoginBean", member);
        Long memberIdx = member.getMemberIdx();
        if (result.hasErrors()) {
            return "board/QNA/write";
        }
        //문의사항 작성 시 필요한 내용은 제목과 내용, 파일도 있네 잠깐만 추가하자
        Qna qna = new Qna();
        qna.setMemberIdx(memberIdx);
        qna.setQnaWriter(qnaDto.getQnaWriter());
        qna.setQnaSubject(qnaDto.getQnaSubject());
        qna.setQnaContent(qnaDto.getQnaContent());
        if (file.isEmpty()) {
            qnaService.writeQna(qna);
        } else {
            qnaService.writeQnaFile(qna, file);
        }
        return "redirect:read?qnaIdx=" + qna.getQnaIdx();

    }

    /*@PutMapping
    public String upViews(){

    }*/

    //QNA 상세페이지
    @GetMapping("/read")
    public String read(@RequestParam("qnaIdx") Long qnaIdx, Model mo,
                       Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        mo.addAttribute("memberLoginBean", member);
        Qna qna = qnaService.get(qnaIdx);
        mo.addAttribute("qnaBean", qna);
        return "board/QNA/read";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("qnaIdx") Long qnaIdx) throws Exception {
        Qna qna = qnaService.get(qnaIdx);
        String projectPath = System.getProperty("user.dir");
        String getFullPath = projectPath + "\\src\\main\\resources\\static\\files";
        UrlResource resource = new UrlResource("file:" + getFullPath + "\\" + qna.getQnaFile());
        String encodedFileName = UriUtils.encode(qna.getQnaFile(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }

    //QNA 답변페이지
    @PutMapping("/answer")
    @ResponseBody
    public void answerQna(Boolean qnaIsanswered,
                          Long qnaIdx,
                          String qnaAnswer,
                          String qnaComplete) {
        Qna qna = qnaService.get(qnaIdx);
        if (qna.getQnaSubject().contains("처리완료")) {
            qna.setQnaSubject(qnaComplete);
        } else {
            qna.setQnaSubject("[처리완료]" + qnaComplete);
        }
        qna.setQnaAnswer(qnaAnswer);
        qna.setQnaIsanswered(qnaIsanswered);
        qnaService.answerQna(qna);
    }

    //QNA 삭제
    @DeleteMapping("/delete")
    public String delete(@RequestParam("qnaIdx") Long qnaIdx
    ) {
        qnaService.deleteQna(qnaIdx);
        return "redirect:/board/qna";
    }

}
