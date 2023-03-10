package com.fmi.nada.board.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/board/notice")
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    //공지사항 메인 페이지
    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("noticeList",noticeService.getallNoticeList());
        return "board/notice/index";
    }

    // 공지사항 글 읽기 ( 조회수 증가 )
    @GetMapping("/read")
    public String read(@PathVariable("notice_idx") Long notice_idx,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {

        Notice notice = noticeService.getNotice(notice_idx);
        viewCountValidation(notice, request, response);
        model.addAttribute("readNoticeBean", notice);
        return "board/notice/read";
    }

    // 공지사항 작성 페이지
    @GetMapping("/write")
    public String writeNotice() {
        return "board/notice/write";
    }

    // 공지사항 작성 서비스 로직 실행
    @PostMapping("/write_pro")
    public String writeNotice_pro(@Valid @ModelAttribute("addNoticeBean") NoticeDTO noticeDTO,
                                  Model model) {
        Notice notice = new Notice();
        notice.setNotice_subject(noticeDTO.getNotice_subject());
        notice.setNotice_content(noticeDTO.getNotice_content());
        notice.setNotice_file(noticeDTO.getNotice_file());
        noticeService.registerNotice(notice);
        return "redirect:list";
    }

    // 공지사항 수정 폼 페이지
    @GetMapping("/modify")
    public String modifyNotice(@RequestParam("notice_idx") Long notice_idx, Model model) {
        Notice notice = noticeService.getNotice(notice_idx);
        model.addAttribute("modifyNoticeBean", notice);
        return "board/notice/modify";
    }

    // 공지사항 수정 서비스 로직 수행
    @PutMapping("/modify_pro")
    public String modify_proNotice(@Valid @ModelAttribute("modifyNoticeBean") NoticeDTO noticeDTO,
                                   @RequestParam("notice_idx") Long notice_idx) {
        Notice notice = noticeService.getNotice(notice_idx);
        notice.setNotice_subject(noticeDTO.getNotice_subject());
        notice.setNotice_content(noticeDTO.getNotice_content());
        noticeService.updateNotice(notice);
        return "redirect:read";
    }

    // 공지사항 삭제 서비스 로직
    @DeleteMapping("/delete")
    public String deleteNotice(@RequestParam("notice_idx") Long notice_idx) {
        noticeService.deleteNotice(notice_idx);
        return "redirect:list";
    }

    // 조회수 증가에 대한 중복 조회 방지 메서드 ( 24시간 기준으로 여러번 방문해도 조회수는 1 증가 )
    private void viewCountValidation(Notice notice, HttpServletRequest request, HttpServletResponse response) {
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
                if (!cookie.getValue().contains("[" + notice.getNotice_idx() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    notice.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + notice.getNotice_idx() + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 noticeView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            notice.addViewCount();
            cookie = new Cookie("noticeView", "[" + notice.getNotice_idx() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
