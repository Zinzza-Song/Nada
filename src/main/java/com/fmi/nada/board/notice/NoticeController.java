package com.fmi.nada.board.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 공지사항 컨트롤러
 */
@Controller
@RequestMapping("/board/notice")
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    //공지사항 메인 페이지
    @GetMapping
    public String main(@PageableDefault Pageable pageable,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       Model model) {

        Page<Notice> noticeList = null;

        if (keyword == null) {
            noticeList = noticeService.findAllByOrderByNoticeDateDesc(pageable);
        } else if (type.equals("subject") && keyword != null) {
            noticeList = noticeService.findAllByNoticeSubjectContaining(keyword, pageable);
        } else if (type.equals("content") && keyword != null) {
            noticeList = noticeService.findAllByNoticeContentContaining(keyword, pageable);
        }

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "board/notice/index";
    }

    // 공지사항 글 읽기 ( 조회수 증가 )
    @GetMapping("/read")
    public String read(@RequestParam("noticeIdx") Long noticeIdx, Model model) {
        Notice notice = noticeService.getNoticeDetail(noticeIdx);
        notice.setNoticeViews(notice.getNoticeViews() + 1L);
        noticeService.updateNotice(notice);
        model.addAttribute("readNoticeBean", notice);
        return "board/notice/read";
    }

    // 공지사항 작성 페이지
    @GetMapping("/write")
    public String writeNotice(@ModelAttribute("addNoticeBean") NoticeDTO noticeDTO) {
        return "board/notice/write";
    }

    // 공지사항 작성 서비스 로직 실행
    @PostMapping("/write_pro")
    public String writeNotice_pro(@Valid @ModelAttribute("addNoticeBean") NoticeDTO noticeDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "board/notice/write";
        }
        Notice notice = new Notice();
        notice.setNoticeSubject(noticeDTO.getNoticeSubject());
        notice.setNoticeContent(noticeDTO.getNoticeContent());
        notice.setNoticeViews(0L);
        if (file.isEmpty()) {
            noticeService.writeNotice(notice);
        } else {
            noticeService.writeNoticeFile(notice, file);
        }
        return "redirect:read?noticeIdx=" + notice.getNoticeIdx();
    }

    // 공지사항 수정 폼 페이지
    @GetMapping("/modify")
    public String modifyNotice(@RequestParam("noticeIdx") Long notice_idx, Model model) {
        Notice notice = noticeService.getNoticeDetail(notice_idx);
        model.addAttribute("modifyNoticeBean", notice);
        model.addAttribute("readNoticeBean", notice);
        return "board/notice/modify";
    }

    // 공지사항 수정 서비스 로직 수행
    @PutMapping("/modify_pro")
    public String modify_proNotice(@Valid @ModelAttribute("modifyNoticeBean") NoticeDTO noticeDTO,
                                   BindingResult bindingResult,
                                   @RequestParam("noticeIdx") Long noticeIdx,
                                   MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "board/notice/modify";
        }
        Notice notice = noticeService.getNoticeDetail(noticeIdx);
        notice.setNoticeSubject(noticeDTO.getNoticeSubject());
        notice.setNoticeContent(noticeDTO.getNoticeContent());
        if (file.isEmpty()) {
            noticeService.updateNotice(notice);
        } else {
            noticeService.updateNoticeFile(notice, file);
        }
        return "redirect:read?noticeIdx=" + noticeIdx;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("noticeIdx") Long noticeIdx) throws Exception {
        Notice notice = noticeService.getNoticeDetail(noticeIdx);
        String projectPath = System.getProperty("user.dir");
        String getFullPath = projectPath + "\\src\\main\\resources\\static\\files";
        UrlResource resource = new UrlResource("file:" + getFullPath + "\\" + notice.getNoticeFile());
        String encodedFileName = UriUtils.encode(notice.getNoticeFile(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }

    // 공지사항 삭제 서비스 로직
    @DeleteMapping("/delete")
    public String deleteNotice(@RequestParam("noticeIdx") Long noticeIdx) {
        noticeService.deleteNotice(noticeIdx);
        return "redirect:/board/notice";
    }

}
