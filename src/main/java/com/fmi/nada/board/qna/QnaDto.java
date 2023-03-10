package com.fmi.nada.board.qna;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QnaDto {

    @NotBlank
    private String qna_content;
    @NotBlank
    private String qna_subject;
    @NotBlank
    private String qna_answer;


}
