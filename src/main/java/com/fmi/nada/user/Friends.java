package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 친구 목록 엔티티
 */
@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Friends {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "friends_idx")
    private Long friendsIdx;

    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "friends_nickname")
    private String friendsNickname;

    @CreatedDate
    @Column(name = "friends_date")
    private LocalDateTime friendsDate;

}
