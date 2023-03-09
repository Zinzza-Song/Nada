package com.fmi.nada.board.qna;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaDto {
    private Long qna_idx;
    private String qna_answer;
    private boolean qna_isanswered;
}
