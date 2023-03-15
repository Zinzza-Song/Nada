package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Keyword Entity
 * */

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "keyword_idx")
    private Long keywordIdx;

    @Column(name = "keyword_name")
    private String keywordName;

    @Column(name = "keyword_cnt", insertable = false, updatable = false, columnDefinition = "int default 1")
    private Long keywordCnt;

}
