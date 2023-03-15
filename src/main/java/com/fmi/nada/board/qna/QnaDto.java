package com.fmi.nada.board.qna;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * QnA DTO
 */
@Getter
@Setter
public class QnaDto {

    @NotBlank
    private String qnaContent;
    @NotBlank
    private String qnaSubject;
    @NotBlank
    private String qnaAnswer;
}
