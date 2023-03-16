package com.fmi.nada.board.qna;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * QnA Entity
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Qna {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "qna_idx")
    private Long qnaIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "qna_subject")
    private String qnaSubject;

    @Column(name = "qna_writer")
    private String qnaWriter;

    @Column(name = "qna_date")
    @CreatedDate
    private LocalDateTime qnaDate;

    @Column(name = "qna_content")
    private String qnaContent;

    @Column(name = "qna_file")
    private String qnaFile;

    @Column(name = "qna_views", columnDefinition = "int default 0")
    private Long qnaViews;

    @Column(name = "qna_answer")
    private String qnaAnswer;

    @Column(name = "qna_isanswered")
    private Boolean qnaIsanswered = false;


}
