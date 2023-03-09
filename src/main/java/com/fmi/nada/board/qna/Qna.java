package com.fmi.nada.board.qna;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Qna {

    @Id
    private Long qna_idx;
    private Long member_idx;
    private String qna_subject;
    private String qna_writer;
    @CreatedDate
    private LocalDateTime qna_date;
    private String qna_content;
    private String qna_file;
    @Column(insertable = false, updatable = false, columnDefinition = "int default 0")
    private Long qna_views;
    private String qna_answer;
    private boolean qna_isanswered;


}
