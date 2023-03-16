package com.fmi.nada.board.qna;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * QnA DTO
 */
@Getter
@Setter
public class QnaAnswerDto {

    @NotBlank
    private String qnaAnswer;

}
