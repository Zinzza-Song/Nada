package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 레포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String email);

    Member findByMemberIdx(Long memberIdx);

    List<Member> findAllByOrderByMemberJoinDateDesc();

}
