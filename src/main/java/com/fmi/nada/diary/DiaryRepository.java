package com.fmi.nada.diary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 다이어리 레포지토리
 */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByDiaryIdx(Long DiaryIdx);

    List<Diary> findTop5ByOrderByDiaryDateDesc();

    List<Diary> findTop5ByOrderByDiarySympathyCntDesc();

    Diary findByDiarySubject(String diarySubject);

    // 최신 다이어리 게시글부터 정렬
    Page<Diary> findAllByOrderByDiaryDateDesc(Pageable pageable);

    List<Diary> findMyDiaryByMemberIdx(Long memberIdx);
}
