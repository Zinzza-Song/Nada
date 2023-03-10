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
public class Analyze {

    @Id
    @GeneratedValue
    private Long analyze_idx;

    private Long diary_idx;

    private String analyze_result;

    private Integer analyze_score;

}
