package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 댓글 레포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 다이어리 상세정보에서 볼 수 있는 댓글 ( 최신순 )
    List<Comment> findAllByDiaryIdxOrderByCommentDateDesc(Long diaryIdx);
}
