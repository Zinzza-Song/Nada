package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment Repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 다이어리 상세정보에서 볼 수 있는 댓글 ( 최신순 )
    List<Comment> findAllByDiaryIdxOrderByCommentDateDesc(Long diaryIdx);

    // 전체 댓글 조회
    List<Comment> findAllByOrderByCommentDateDesc();

    // 댓글 삭제
    void deleteByCommentIdx(Long commentIdx);

}
