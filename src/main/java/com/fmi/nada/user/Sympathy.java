package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 일기 공감 목록 엔티티
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sympathy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sympathy_idx")
    private Long sympathyIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "diary_idx")
    private Long diaryIdx;

    public Sympathy(Long memberIdx, Long diaryIdx) {
        this.memberIdx = memberIdx;
        this.diaryIdx = diaryIdx;
    }

}
