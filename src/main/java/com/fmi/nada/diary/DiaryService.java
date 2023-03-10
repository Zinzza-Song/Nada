package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void registerDiary(Diary diary) {
        diaryRepository.save(diary);

    }


}
