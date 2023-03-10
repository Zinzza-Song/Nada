package com.fmi.nada.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class Member implements UserDetails {

    @GeneratedValue
    @Id
    private Long member_idx;

    private String member_name;

    private String member_nickname;

    private String member_birth;

    private String member_address;

    private String username;

    private String password;

    private String member_phone;

    @ColumnDefault("50")
    private Integer member_emotion_score;

    @CreatedDate
    private LocalDateTime member_join_date;

    @CreatedDate
    private LocalDateTime member_login_date;

    @Column(name = "member_authority")
    private String authority;

    public Member(
            String username,
            String password,
            String authority,
            String member_name,
            String member_nickname,
            String member_birth,
            String member_address,
            String member_phone
    ) {
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.member_name = member_name;
        this.member_nickname = member_nickname;
        this.member_birth = member_birth;
        this.member_address = member_address;
        this.member_phone = member_phone;
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
