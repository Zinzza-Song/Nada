package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Analyzed Entity
 */
@Entity
@Getter
@Setter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Analyzed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analyze_idx")
    private Long analyzeIdx;

    @Column(name = "diary_idx")
    private Long diaryIdx;

    @Lob
    @Column(name = "analyze_result")
    private String analyzeResult;

    @Column(name = "analyze_score")
    private Integer analyzeScore;

    public Analyzed(Long diaryIdx, String analyzeResult, Integer analyzeScore) {
        this.diaryIdx = diaryIdx;
        this.analyzeResult = analyzeResult;
        this.analyzeScore = analyzeScore;
    }

}
