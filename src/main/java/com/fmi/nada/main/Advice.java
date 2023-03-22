package com.fmi.nada.main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 오늘의 조언 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advice {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "advice_idx")
    private Long adviceIdx;

    @Column(name = "advice_content")
    private String adviceContent;

    @Column(name = "advice_author")
    private String adviceAuthor;

}
