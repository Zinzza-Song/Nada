package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 키워드 테이블 엔티티
 * */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @GeneratedValue
    @Id
    private Long keyword_idx;

    private String keyword_name;

    public Keyword(String keyword_name) {
        this.keyword_name = keyword_name;
    }

}
