package com.fmi.nada.admin;

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
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_idx")
    private Long logIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "log_issuccessed")
    private Boolean logIssuccessed;

    @Column(name = "log_memberemail")
    private String logMemberEmail;

    @CreatedDate
    @Column(name = "log_date")
    private LocalDateTime logDate;

    @Column(name = "log_usedservice")
    private String logUsedService;

    public Log(
            Long memberIdx,
            Boolean logIssuccessed,
            String logMemberEmail,
            String logUsedService) {
        this.memberIdx = memberIdx;
        this.logIssuccessed = logIssuccessed;
        this.logMemberEmail = logMemberEmail;
        this.logUsedService = logUsedService;
    }

}
