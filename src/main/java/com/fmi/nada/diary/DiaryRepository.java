package com.fmi.nada.diary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Diary Repository
 */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByDiaryIdx(Long DiaryIdx);

    List<Diary> findTop5ByOrderByDiaryDateDesc();

    List<Diary> findTop5ByOrderByDiarySympathyCntDesc();

    Diary findByDiarySubject(String diarySubject);

    // 최신 다이어리 게시글부터 정렬
    Page<Diary> findAllByOrderByDiaryDateDesc(Pageable pageable);
    List<Diary> findAllByOrderByDiaryDateDesc();

    List<Diary> findMyDiaryByMemberIdx(Long memberIdx);

    List<Diary> findLikeDiaryByDiaryIdx(Long diaryIdx);

    // 검색 기능 페이징에 사용되는 JPQL
    Page<Diary> findAllByDiaryWriterContaining(String diaryWriter, Pageable pageable);
    Page<Diary> findAllByDiaryContentContaining(String diaryContent, Pageable pageable);
    Page<Diary> findAllByDiaryKeywordsContaining(String diaryKeywords, Pageable pageable);
}
