package com.fmi.nada.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 오늘의 조언 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advice {

    @GeneratedValue
    @Id
    private Long advice_idx;

    private String advice_content;
    private String advice_author;

}
