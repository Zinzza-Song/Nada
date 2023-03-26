package com.fmi.nada.board.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 공지사항 레포지토리
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByOrderByNoticeDateDesc(Pageable pageable);

    Page<Notice> findAllByNoticeSubjectContaining(String noticeSubject, Pageable pageable);

    Page<Notice> findAllByNoticeContentContaining(String noticeContent, Pageable pageable);
}
