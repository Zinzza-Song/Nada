package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Keyword Repository
 */
@Transactional
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Keyword findByKeywordName(String keywordName);

    List<Keyword> findTop5ByOrderByKeywordCntDesc();

}
