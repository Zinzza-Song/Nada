package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 일기 공감 목록 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sympathy {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "like_idx")
    private Long likeIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "comment_idx")
    private Long commentIdx;

}
