package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword findByKeywordName(String keywordName) {
        return keywordRepository.findByKeywordName(keywordName);
    }

    public List<Keyword> findTop5ByOrderByKeywordCntDesc() {
        return keywordRepository.findTop5ByOrderByKeywordCntDesc();
    }

    public void register(Keyword keyword){
        keywordRepository.save(keyword);
    }

}
