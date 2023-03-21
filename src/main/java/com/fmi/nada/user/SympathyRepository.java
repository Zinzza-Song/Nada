package com.fmi.nada.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 공감 레포지토리
 */
public interface SympathyRepository extends JpaRepository<Sympathy, Long> {

    Sympathy findByMemberIdx(Long memberIdx);

    Sympathy findByMemberIdxAndDiaryIdx(Long memberIdx, Long diaryIdx);

}
