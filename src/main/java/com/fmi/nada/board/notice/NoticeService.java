package com.fmi.nada.board.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void registerNotice(Notice notice) {
        noticeRepository.save(notice);
    }

    //공지사항 글 상세 페이지에서 보여지는 공지사항 글
    public Notice getNotice(Long notice_idx) {
        Optional<Notice> noticeOptional = noticeRepository.findById(notice_idx);
        return noticeOptional.get();
    }

    //공지사항 삭제
    public void deleteNotice(Long notice_idx) {
        noticeRepository.deleteById(notice_idx);
        
    }

    //공지사항 수정
    public void updateNotice(Notice notice) {
        noticeRepository.save(notice);
    }
}
