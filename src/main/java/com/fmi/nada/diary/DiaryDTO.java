package com.fmi.nada.diary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 다이어리 DTO
 * */
@Getter
@Setter
public class DiaryDTO {

    @NotBlank(message = "제목을 반드시 입력해 주세요")
    private String diary_subject;

    private String diary_writer;

    @NotBlank(message = "일기 내용을 반드시 작성해 주세요")
    private String diary_content;

    @NotBlank(message = "1개 이상은 무조건 들어가야합니다.")
    private String diary_keywords;

    private Boolean diary_publicable;

    @NotBlank
    private String diary_analyze;

    private Boolean diary_analyze_publicable;


}
