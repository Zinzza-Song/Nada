package com.fmi.nada.user;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 회원 엔티티
 */
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Builder
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "member_idx")
    private Long memberIdx;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "member_birth")
    private String memberBirth;

    @Column(name = "member_address")
    private String memberAddress;

    @Column(name = "member_email")
    private String username;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_phone")
    private String memberPhone;

    @Column(name = "member_emotion_score")
    @ColumnDefault("50")
    private Integer memberEmotionScore;

    @Column(name = "member_join_date")
    @CreatedDate
    private LocalDateTime memberJoinDate;

    @Column(name = "member_login_date")
    @CreatedDate
    private LocalDateTime memberLoginDate;

    @Column(name = "member_authority")
    private String authority;

    @Column(name = "member_social")
    private SocialType socialType;

    @Column(name = "member_socialId")
    private String socialId;

    @Column(name = "member_refreshToken")
    private String refreshToken;

    public Member(
            String username,
            String password,
            String authority,
            String memberName,
            String memberNickname,
            String memberBirth,
            String memberAddress,
            String memberPhone
    ) {
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.memberName = memberName;
        this.memberNickname = memberNickname;
        this.memberBirth = memberBirth;
        this.memberAddress = memberAddress;
        this.memberPhone = memberPhone;
    }

    public Boolean isAdmin() {
        return authority.equals("ROLE_ADMIN");
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void authorizeUser() {
        this.authority = "ROLE_USER";
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

}
