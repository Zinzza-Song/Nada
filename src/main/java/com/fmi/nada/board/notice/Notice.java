package com.fmi.nada.board.notice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue
    private Integer notice_idx;

    @NotBlank
    private String notice_subject;

    @CreatedDate
    private LocalDateTime notice_date;

    @NotBlank
    private String notice_content;

    private String notice_file;

    private Long notice_views;

    public void addViewCount() {
        this.notice_views++;
    }

}
