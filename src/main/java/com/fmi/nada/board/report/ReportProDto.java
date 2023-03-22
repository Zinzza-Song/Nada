package com.fmi.nada.board.report;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReportProDto {

    @NotBlank(message = "처리결과를 작성해주세요.")
    private String reportAdminMent;

}
