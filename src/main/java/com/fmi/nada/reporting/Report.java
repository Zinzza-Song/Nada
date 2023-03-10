package com.fmi.nada.reporting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue
    private Long report_idx;

    private Long member_idx;

    private String report_subject;

    private String report_writer;

    @CreatedDate
    private LocalDateTime report_date;

    private String report_category;

    private String report_content;

    private String reported_member;

    private String report_file;

    private Boolean report_iscompleted = false;

    private String report_adminment;



}
