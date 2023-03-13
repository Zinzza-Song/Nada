package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 다이어리의 분석 결과 서비스
 */
@Service
@RequiredArgsConstructor
public class AnalyzedService {

    private final AnalyzedRepository analyzedRepository;

    public void resiterAnalyze(Long diaryIndex,
                               String analyzeResult,
                               Integer analyzeScore){
        analyzedRepository.save(new Analyzed(
                diaryIndex,
                analyzeResult,
                analyzeScore));
    }


    public Analyzed getAnalyzed(Long boardIndex) {
        return analyzedRepository.getAnalyzedByBoardIdx(boardIndex);
    }

}
