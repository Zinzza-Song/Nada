package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword findByKeyword_name(String keyword_name) {
        return keywordRepository.findByKeyword_name(keyword_name);
    }

    public void register(Keyword keyword){
        keywordRepository.save(keyword);
    }

}
