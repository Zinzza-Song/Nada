package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Comment Repository
 */
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 다이어리 상세정보에서 볼 수 있는 댓글 ( 최신순 )
    List<Comment> findAllByDiaryIdxOrderByCommentDateDesc(Long diaryIdx);

    // 전체 댓글 조회
    List<Comment> findAllByOrderByCommentDateDesc();

    // 댓글 검색
    List<Comment> findAllByCommentContentContainingOrderByCommentDateDesc(String commentContent);

    List<Comment> findAllByCommentWriterContainingOrderByCommentDateDesc(String commentWriter);

    // 댓글 삭제
    void deleteByCommentIdx(Long commentIdx);

    Comment findByDiaryIdxAndCommentIdxAndMemberIdx(Long diaryIdx, Long commentIdx, Long memberIdx);
}
