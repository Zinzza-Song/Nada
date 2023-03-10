package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 키워드 테이블 엔티티
 * */

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @GeneratedValue
    @Id
    private Long keyword_idx;

    private String keyword_name;

    @Column(insertable = false, updatable = false, columnDefinition = "int default 1")
    private Long keyword_cnt;

}
