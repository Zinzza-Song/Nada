package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 다이어리 서비스
 * */
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public List<Diary> getDiaryList() {
        List<Diary> diaryList = diaryRepository.findAll();
        return diaryList;
    }

    public void registerDiary(Long member_idx,
                              String diary_subject,
                              String diary_writer,
                              String diary_content,
                              String diary_keywords,
                              String diary_analyze,
                              Boolean diary_publicable,
                              Boolean diary_analyze_publicable
    ) {
        diaryRepository.save(new Diary(
                member_idx,
                diary_subject,
                diary_writer,
                diary_content,
                diary_keywords,
                diary_analyze,
                diary_publicable,
                diary_analyze_publicable));

    }

    public Diary findByDiary_subject(String diarySubject) {
        return diaryRepository.findByDiarySubject(diarySubject);
    }

    public List<Diary> findTop5ByOrderByDiaryDateDesc() {
        return diaryRepository.findTop5ByOrderByDiaryDateDesc();
    }

    public List<Diary> findTop5ByOrderByDiarySympathyCntDesc() {
        return diaryRepository.findTop5ByOrderByDiarySympathyCntDesc();
    }

}
