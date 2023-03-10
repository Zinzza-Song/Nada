package com.fmi.nada.diary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * 댓글 테이블 엔티티
 * */
@Entity
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue
    private Long comment_idx;

    private Long diary_idx;

    private Long member_idx;

    private String comment_content;

    private Integer comment_like_cnt;

    @CreatedDate
    private LocalDateTime comment_date;

    private String comment_writer;

    private String comment_writeremail;
}
