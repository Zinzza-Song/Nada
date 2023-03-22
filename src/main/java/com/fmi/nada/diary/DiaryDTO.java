package com.fmi.nada.diary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Diary DTO
 */
@Getter
@Setter
public class DiaryDTO {


    @NotBlank(message = "제목을 반드시 입력해 주세요")
    private String diarySubject;

    private String diaryWriter;

    @NotBlank(message = "일기 내용을 반드시 작성해 주세요")
    private String diaryContent;

    @NotBlank(message = "1개 이상 고르셔야합니다.")
    private String diaryKeywords;

    private Boolean diaryPublicable;

    private String diaryAnalyze;

    private Boolean diaryAnalyzePublicable;

    private String analyzeScore;

}
