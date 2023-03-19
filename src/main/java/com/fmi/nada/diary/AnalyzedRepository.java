package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Analyzed Repository
 * */
public interface AnalyzedRepository extends JpaRepository<Analyzed, Long> {
    @Query("SELECT a FROM Analyzed a JOIN FETCH a.diary d WHERE d.memberIdx = :memberIdx")
    List<Analyzed> findTop6ByByDiaryMemberIdx(@Param("memberIdx") Long memberIdx);
}
