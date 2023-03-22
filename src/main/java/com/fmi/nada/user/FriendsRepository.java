package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 친구 레포지토리
 */
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    @Transactional
    void deleteFriendsByMemberIdxAndFriendsMemberIdx(Long memberIdx, Long friendsMemberIdx);

    List<Friends> findByMemberIdxAndFriendsMemberIdx(Long memberIdx, Long friendsMemberIdx);

    List<Friends> findFriendsByMemberIdx(Long memberIdx);

}
