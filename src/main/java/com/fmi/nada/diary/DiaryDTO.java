package com.fmi.nada.diary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DiaryDTO {

    @NotBlank
    private String diary_subject;

    @NotBlank
    private String diary_content;

    private Boolean diary_publicable;

    @NotBlank
    private String diary_analyze;

    private Boolean diary_analyze_publicable;


}
