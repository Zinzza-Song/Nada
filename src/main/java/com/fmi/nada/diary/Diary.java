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

/**
 * Diary Entity
 * */
@Entity
@Getter
@Setter
@Table
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_idx")
    private Long diaryIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "diary_subject")
    private String diarySubject;

    @Column(name = "diary_writer")
    private String diaryWriter;

    @Column(name = "diary_content")
    private String diaryContent;

    @CreatedDate
    @Column(name = "diary_date")
    private LocalDateTime diaryDate;

    @Column(name = "diary_keywords")
    private String diaryKeywords;

    @Column(name = "diary_analyze")
    private String diaryAnalyze;

    @Column(name = "diary_publicable")
    private Boolean diaryPublicable;

    @Column(name = "diary_analyze_publicable")
    private Boolean diaryAnalyzePublicable;

    @ColumnDefault("0")
    @Column(name = "diary_cnt")
    private Long diaryCnt;

    @ColumnDefault("0")
    @Column(name = "diary_sympathy_cnt")
    private Long diarySympathyCnt;

    public Diary(Long memberIdx,
                 String diarySubject,
                 String diaryWriter,
                 String diaryContent,
                 String diaryKeywords,
                 String diaryAnalyze,
                 Boolean diaryPublicable,
                 Boolean diaryAnalyzePublicable
                 ) {
        this.memberIdx = memberIdx;
        this.diarySubject = diarySubject;
        this.diaryWriter = diaryWriter;
        this.diaryContent = diaryContent;
        this.diaryKeywords = diaryKeywords;
        this.diaryAnalyze = diaryAnalyze;
        this.diaryPublicable = diaryPublicable;
        this.diaryAnalyzePublicable = diaryAnalyzePublicable;
    }

    public void addViewCount() {
        this.diaryCnt++;
    }

}
