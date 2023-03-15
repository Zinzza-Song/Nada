package com.fmi.nada.board.report;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 신고글 처리를 위한 DTO
 */
@Getter
@Setter
public class ReportDto {

    @NotBlank(message = "제목을 작성해주세요.")
    private String reportSubject;

    private String reportCategory;

    @NotBlank(message = "신고 내용을 작성해주세요.")
    private String reportContent;

    private String reportReportedMember;

    private String reportFile;

    private Boolean reportIsCompleted;

}
