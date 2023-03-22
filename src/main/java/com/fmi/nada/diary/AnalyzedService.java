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
                                Integer analyzeScore) {
        analyzedRepository.save(new Analyzed(
                diaryIndex,
                analyzeResult,
                analyzeScore));
    }

    public List<Analyzed> findTop6ByDiaryMemberIdx(Long memberIdx) {
        List<Analyzed> top6Analyzed = analyzedRepository.findTop6ByDiaryMemberIdx(memberIdx);

        if (top6Analyzed.size() > 6) {
            top6Analyzed = top6Analyzed.subList(0, 6);
        }
        return top6Analyzed;
    }

}
