package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue
    private Long diary_idx;

    private Long member_idx;

    private String diary_subject;

    private String diary_writer;

    private String diary_content;

    @CreatedDate
    private LocalDateTime diary_date;

    private String diary_keywords;

    private String diary_analyze;

    private Boolean diary_publicable;

    private Boolean diary_analyze_publicable;

    @ColumnDefault("0")
    private Long diary_cnt;

    @ColumnDefault("0")
    private Long diary_sympathy_cnt;

    public Diary(Long member_idx,
                 String diary_subject,
                 String diary_writer,
                 String diary_content,
                 String diary_keywords,
                 String diary_analyze,
                 Boolean diary_publicable,
                 Boolean diary_analyze_publicable
                 ) {
        this.member_idx = member_idx;
        this.diary_subject = diary_subject;
        this.diary_writer = diary_writer;
        this.diary_content = diary_content;
        this.diary_keywords = diary_keywords;
        this.diary_analyze = diary_analyze;
        this.diary_publicable = diary_publicable;
        this.diary_analyze_publicable = diary_analyze_publicable;
    }

}
