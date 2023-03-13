package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
/**
 * 분석 테이블 엔티티
 * */
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

    @Column(name = "diary_idx")
    private Long diaryIdx;

    @Column(name = "analyze_result")
    private String analyzeResult;

    @Column(name = "analyze_score")
    private Integer analyzeScore;

}
