package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 회원가입 방지 유저 모음 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Ban {

    @GeneratedValue
    @Id
    private Long ban_idx;

    private String ban_email;
    private Boolean ban_isbyadmin;

    @CreatedDate
    private LocalDateTime ban_date;

}
