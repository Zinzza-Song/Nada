package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 친구 레포지토리
 */
public interface FriendsRepository extends JpaRepository<Friends,Long> {

    void deleteFriendsByMemberIdxAndFriendsMemberIdx(Long memberIdx, Long friendsMemberIdx);
    List<Friends> findByMemberIdxAndFriendsMemberIdx(Long memberIdx, Long friendsMemberIdx);

    List<Friends> findFriendsByMemberIdx(Long memberIdx);

}
