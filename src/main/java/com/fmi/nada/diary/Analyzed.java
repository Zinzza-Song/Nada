package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Analyzed Entity
 */
@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Analyzed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analyze_idx")
    private Long analyzeIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_idx")
    private Diary diary;

    @Lob
    @Column(name = "analyze_result")
    private String analyzeResult;

    @Column(name = "analyze_score")
    private Integer analyzeScore;

    public Analyzed(Diary diary, String analyzeResult, Integer analyzeScore) {
        this.diary = diary;
        this.analyzeResult = analyzeResult;
        this.analyzeScore = analyzeScore;
    }

}
