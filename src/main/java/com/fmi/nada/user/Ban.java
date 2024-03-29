package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Ban {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ban_idx")
    private Long banIdx;

    @Column(name = "ban_email")
    private String banEmail;

    @Column(name = "ban_isbyadmin")
    private Boolean banIsbyadmin;

    @CreatedDate
    @Column(name = "ban_date")
    private LocalDateTime banDate;

}
