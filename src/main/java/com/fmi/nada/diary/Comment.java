package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * Comment Entity
 * */
@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx")
    private Long commentIdx;

    @Column(name = "diary_idx")
    private Long diaryIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "comment_like_cnt")
    private Integer commentLikeCnt;

    @CreatedDate
    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    @Column(name = "comment_writer")
    private String commentWriter;

    @Column(name = "comment_writeremail")
    private String commentWriteremail;
}
