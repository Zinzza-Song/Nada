package com.fmi.nada.board.notice;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 공지사항 레포지토리
 * */
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
