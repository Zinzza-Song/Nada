package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 좋아요 목록 엔티티
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "like_idx")
    private Long likeIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "comment_idx")
    private Long commentIdx;

    public Likes(Long memberIdx, Long commentIdx) {
        this.memberIdx = memberIdx;
        this.commentIdx = commentIdx;
    }

}
