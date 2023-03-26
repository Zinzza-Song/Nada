package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Analyzed Repository
 */
@Transactional
public interface AnalyzedRepository extends JpaRepository<Analyzed, Long> {

    @Query("select a from Analyzed a, Diary d where d.memberIdx=:memberIdx order by d.diaryDate desc")
    List<Analyzed> findTop6ByDiaryMemberIdx(@Param("memberIdx") Long memberIdx);

    Analyzed findByDiaryIdx(Long diaryIdx);

}
