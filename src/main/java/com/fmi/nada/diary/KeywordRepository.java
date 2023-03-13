package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 키워드 레포지토리
 * */
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Keyword findByKeywordName(String keywordName);

    List<Keyword> findTop5ByOrderByKeywordCntDesc();

}
