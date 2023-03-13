package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * 키워드 레포지토리
 * */
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    public Keyword findByKeywordName(String keywordName);

}
