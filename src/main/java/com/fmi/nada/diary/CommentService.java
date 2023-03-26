package com.fmi.nada.diary;

import com.fmi.nada.user.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment Service
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    public List<Comment> findAllByDiaryIdxOrderByCommentDateDesc(Long diaryIndex) {
        List<Comment> commentList = commentRepository.findAllByDiaryIdxOrderByCommentDateDesc(diaryIndex);
        return commentList;
    }

    public List<Comment> findAllByOrderByCommentDateDesc() {
        return commentRepository.findAllByOrderByCommentDateDesc();
    }

    public List<Comment> findAllByCommentContentOrderByCommentDateDesc(String keyword) {
        return commentRepository.findAllByCommentContentContainingOrderByCommentDateDesc(keyword);
    }

    public List<Comment> findAllByCommentWriterOrderByCommentDateDesc(String keyword) {
        return commentRepository.findAllByCommentWriterContainingOrderByCommentDateDesc(keyword);
    }
    // 댓글 등록
    public void resisterComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteByCommentIdx(Long commentIdx) {
        commentRepository.deleteByCommentIdx(commentIdx);
        likesRepository.deleteByCommentIdx(commentIdx);
    }

}
