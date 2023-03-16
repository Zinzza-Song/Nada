package com.fmi.nada.board.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * 공지사항 서비스
 * */
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 게시글 전체 리스트
    public Page<Notice> findAllByOrderByNoticeDateDesc(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return noticeRepository.findAllByOrderByNoticeDateDesc(pageable);
    }

    public Page<Notice> findAllByNoticeSubjectContaining(String noticeSubject, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return noticeRepository.findAllByNoticeSubjectContaining(noticeSubject, pageable);
    }
    // 공지사항 등록
    public void registerNotice(Notice notice) {
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
