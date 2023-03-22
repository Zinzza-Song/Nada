package com.fmi.nada.board.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 신고글 Entity
 */
@Entity
@Table
@DynamicInsert
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_idx")
    private Long reportIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "report_subject")
    private String reportSubject;

    @Column(name = "report_writer")
    private String reportWriter;

    @CreatedDate
    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "report_category")
    private String reportCategory;

    @Lob
    @Column(name = "report_content")
    private String reportContent;

    @Column(name = "report_reportedmember")
    private String reportReportedMember;

    @Column(name = "report_file")
    private String reportFile;

    @Column(name = "report_iscompleted")
    @ColumnDefault("false")
    private Boolean reportIsCompleted;

    @Lob
    @Column(name = "report_adminment")
    @ColumnDefault("처리중 입니다.")
    private String reportAdminMent;

    @Column(name = "report_view")
    @ColumnDefault("0")
    private Long reportView;

    public Report(
            Long memberIdx,
            String reportSubject,
            String reportWriter,
            String reportCategory,
            String reportContent,
            String reportReportedMember
    ) {
        this.memberIdx = memberIdx;
        this.reportSubject = reportSubject;
        this.reportWriter = reportWriter;
        this.reportCategory = reportCategory;
        this.reportContent = reportContent;
        this.reportReportedMember = reportReportedMember;
    }

    public void addViewCount() {
        this.reportView++;
    }
}
