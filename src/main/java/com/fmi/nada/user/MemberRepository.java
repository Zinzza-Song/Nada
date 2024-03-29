package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 회원 레포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String name);

    Member findByMemberIdx(Long memberIdx);

    Member findByMemberNickname(String memberNickname);

    List<Member> findByAuthorityOrderByMemberJoinDateDesc(String authority);

    List<Member> findAllByMemberNicknameContainingOrderByMemberJoinDateDesc(String userNickName);

    List<Member> findAllByMemberNameContainingOrderByMemberJoinDateDesc(String memberName);

    List<Member> findAllByMemberBirthContainingOrderByMemberJoinDateDesc(String memberBirth);
}
