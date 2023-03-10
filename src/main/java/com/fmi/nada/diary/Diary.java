package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue
    private Long diary_idx;

    private Long user_idx;

    private String diary_subject;

    private String diary_writer;

    private String diary_content;

    @CreatedDate
    private LocalDateTime diary_date;

    private String diary_keywords;

    private String diary_analyze;

    private Boolean diary_publicable=false;

    private Boolean diary_analyze_publicable=false;

    private Integer diary_cnt;

    private Integer diary_sympathy_cnt;
}
