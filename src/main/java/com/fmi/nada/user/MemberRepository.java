package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 레포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
