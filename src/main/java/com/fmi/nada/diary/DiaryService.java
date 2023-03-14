package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 다이어리 서비스
 */
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public List<Diary> getDiaryList() {
        List<Diary> diaryList = diaryRepository.findAllByOrderByDiaryDateDesc();
        return diaryList;
    }

    public void registerDiary(Long memberIdx,
                              String diarySubject,
                              String diaryWriter,
                              String diaryContent,
                              String diaryKeywords,
                              String diaryAnalyze,
                              Boolean diaryPublicable,
                              Boolean diary_analyzePublicable
    ) {
        diaryRepository.save(new Diary(
                memberIdx,
                diarySubject,
                diaryWriter,
                diaryContent,
                diaryKeywords,
                diaryAnalyze,
                diaryPublicable,
                diary_analyzePublicable));

    }

    public Diary findByDiaryIdx(Long diaryIdx) {
        return diaryRepository.findByDiaryIdx(diaryIdx);
    }

    public void modifyDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public Diary findByDiary_subject(String diarySubject) {
        return diaryRepository.findByDiarySubject(diarySubject);
    }

    public Diary getDiaryDetail(Long diaryIndex) {
        return diaryRepository.getById(diaryIndex);
    }

    public void deleteDiary(Long diaryIdx) {
        diaryRepository.deleteById(diaryIdx);
    }
}
