package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 친구 레포지토리
 */
public interface FriendsRepository extends JpaRepository<Friends,Long> {

    void deleteFriendsByMemberIdxAndFriendsMemberIdx(Long memberIdx, Long friendsMemberIdx);

}
