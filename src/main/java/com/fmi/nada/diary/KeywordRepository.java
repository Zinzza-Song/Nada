package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Keyword Repository
 */
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Keyword findByKeywordName(String keywordName);

    List<Keyword> findTop5ByOrderByKeywordCntDesc();

}
