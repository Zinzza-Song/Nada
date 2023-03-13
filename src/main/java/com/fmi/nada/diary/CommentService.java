package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 서비스
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getCommentListByDiaryIndex(Long diaryIndex) {
        List<Comment> commentList = commentRepository.getCommentListByDiaryIdx(diaryIndex);
        return commentList;
    }
}
