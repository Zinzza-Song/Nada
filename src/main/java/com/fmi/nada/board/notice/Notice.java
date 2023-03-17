package com.fmi.nada.board.notice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 공지사항 엔티티
 * */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_idx")
    private Long noticeIdx;

    @Column(name = "notice_subject")
    private String noticeSubject;

    @CreatedDate
    @Column(name = "notice_date")
    private LocalDateTime noticeDate;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "notice_file")
    private String noticeFile;

    @Column(name = "notice_views")
    @ColumnDefault("0")
    private Long noticeViews;

    public void addViewCount() {
        this.noticeViews++;
    }

}
