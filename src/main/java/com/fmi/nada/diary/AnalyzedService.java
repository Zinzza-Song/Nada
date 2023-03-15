package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Analyzed Service
 */
@Service
@RequiredArgsConstructor
public class AnalyzedService {

    private final AnalyzedRepository analyzedRepository;

    public void resisterAnalyze(Long diaryIndex,
                               String analyzeResult,
                               Integer analyzeScore){
        analyzedRepository.save(new Analyzed(
                diaryIndex,
                analyzeResult,
                analyzeScore));
    }



}
