package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 좋아요 목록 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "like_idx")
    private Long likeIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "diary_idx")
    private Long diaryIdx;

}
