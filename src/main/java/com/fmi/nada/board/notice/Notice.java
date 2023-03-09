package com.fmi.nada.board.notice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * 공지사항 엔티티
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue
    private Long notice_idx;

    private String notice_subject;

    @CreatedDate
    private LocalDateTime notice_date;

    private String notice_content;

    private String notice_file;

    private Long notice_views;

    public void addViewCount() {
        this.notice_views++;
    }

}
