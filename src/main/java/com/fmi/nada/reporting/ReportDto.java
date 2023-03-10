package com.fmi.nada.reporting;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReportDto {

    @NotBlank
    private String report_subject;

    @NotBlank
    private String report_content;

    @NotBlank
    private String report_adminment;

}
