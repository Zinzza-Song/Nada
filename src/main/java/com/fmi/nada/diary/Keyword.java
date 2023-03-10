package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< Updated upstream
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 키워드 테이블 엔티티
 * */
=======
import javax.persistence.*;

>>>>>>> Stashed changes
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
    /*public Keyword(String keyword_name) {
        this.keyword_name = keyword_name;
    }*/

}
