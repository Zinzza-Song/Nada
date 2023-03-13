package com.fmi.nada.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 다이어리 레포지토리
 * */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

   Diary findByDiarySubject(String diarySubject);
   
   // 최신 다이어리 게시글부터 정렬
   List<Diary> findAllByOrderByDiaryDateDesc();

}
