package com.fmi.nada.board.notice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Valid
public class NoticeDTO {

    @NotBlank
    private String notice_subject;

    @NotBlank
    private String notice_content;

    private String notice_file;
}
