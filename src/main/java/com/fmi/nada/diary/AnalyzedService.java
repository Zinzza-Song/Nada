package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Analyzed Service
 */
@Service
@RequiredArgsConstructor
public class AnalyzedService {

    private final AnalyzedRepository analyzedRepository;

    public void resisterAnalyze(Diary diaryIndex,
                               String analyzeResult,
                                Integer analyzeScore){
        analyzedRepository.save(new Analyzed(
                diaryIndex,
                analyzeResult,
                analyzeScore));
    }

    public List<Analyzed> findTop6ByByDiaryMemberIdx(Long memberIdx){
        return analyzedRepository.findTop6ByByDiaryMemberIdx(memberIdx);
    }
}
