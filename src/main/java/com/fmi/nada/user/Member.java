package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 회원 엔티티
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class Member implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    public Boolean isAdmin() {
        return authority.equals("ROLE_ADMIN");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
