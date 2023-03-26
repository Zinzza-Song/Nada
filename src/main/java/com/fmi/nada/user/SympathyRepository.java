package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공감 레포지토리
 */
public interface SympathyRepository extends JpaRepository<Sympathy, Long> {

    Sympathy findByMemberIdx(Long memberIdx);

    @Transactional
    void deleteByDiaryIdxAndMemberIdx(Long diaryIdx, Long memberIdx);

    Sympathy findByMemberIdxAndDiaryIdx(Long memberIdx, Long diaryIdx);

    void deleteByDiaryIdx(Long diaryIdx);

}
