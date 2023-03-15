package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 회원 레포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String name);
    List<Member> findAllByOrderByMemberJoinDateDesc();
    void deleteMemberByMemberIdx(Long memberIdx);

    List<Friends> findFriendsByMemberIdx(Long memberIdx);
    List<BlockList> findBlockListByMemberIdx(Long memberIdx);

}
