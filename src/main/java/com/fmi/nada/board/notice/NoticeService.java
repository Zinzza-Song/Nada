package com.fmi.nada.board.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 공지사항 서비스
 * */
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 게시글 전체 리스트
    public List<Notice> getallNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        return noticeList;
    }

    // 공지사항 등록
    public void registerNotice(Notice notice, MultipartFile file) throws Exception {
        String projectPath = "D:/test";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath+fileName);
        file.transferTo(saveFile);
        notice.setNoticeFile(fileName);

        noticeRepository.save(notice);
    }

    //공지사항 글 상세 페이지에서 보여지는 공지사항 글
    public Notice getNoticeDetail(Long noticeIdx) {
        Optional<Notice> noticeOptional = noticeRepository.findById(noticeIdx);
        return noticeOptional.get();
    }

    //공지사항 삭제
    public void deleteNotice(Long noticeIdx) {
        noticeRepository.deleteById(noticeIdx);
        
    }

    //공지사항 수정
    public void updateNotice(Notice notice) {
        noticeRepository.save(notice);
    }
}
