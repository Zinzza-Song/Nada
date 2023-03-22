package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    void deleteByCommentIdxAndMemberIdx(Long commentIdx, Long memberIdx);

    Likes findByMemberIdxAndCommentIdx(Long memberIdx, Long commentIdx);

}
