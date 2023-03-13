package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword findByKeyword_name(String keywordName) {
        return keywordRepository.findByKeywordName(keywordName);
    }

    public void register(Keyword keyword){
        keywordRepository.save(keyword);
    }

}
