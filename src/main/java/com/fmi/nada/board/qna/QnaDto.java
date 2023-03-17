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
    private String qnaWriter;
    @NotBlank
    private String qnaSubject;
    private String qnaFile;
}
