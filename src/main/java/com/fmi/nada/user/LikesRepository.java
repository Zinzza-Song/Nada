package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LikesRepository extends JpaRepository<Likes, Long> {

    void deleteByCommentIdxAndMemberIdxAndDiaryIdx(Long commentIdx, Long memberIdx, Long diaryIdx);

    Likes findByMemberIdxAndCommentIdx(Long memberIdx, Long commentIdx);

    void deleteByCommentIdx(Long commentIdx);

}
