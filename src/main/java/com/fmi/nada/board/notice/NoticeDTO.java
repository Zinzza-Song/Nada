package com.fmi.nada.board.notice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 공지사항 작성 DTO
 */
@Getter
@Setter
public class NoticeDTO {

    @NotBlank
    private String noticeSubject;

    @NotBlank
    private String noticeContent;

    private String noticeFile;
}
