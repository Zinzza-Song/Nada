package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 다이어리 레포지토리
 */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByDiaryIdx(Long DiaryIdx);

   List<Diary> findTop5ByOrderByDiaryDateDesc();
   List<Diary> findTop5ByOrderByDiarySympathyCntDesc();

   // 최신 다이어리 게시글부터 정렬

    Diary findByDiarySubject(String diarySubject);

    // 최신 다이어리 게시글부터 정렬
    List<Diary> findAllByOrderByDiaryDateDesc();

}
