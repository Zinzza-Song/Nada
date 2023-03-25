package com.fmi.nada.diary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Diary Repository
 */
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByDiaryIdx(Long DiaryIdx);

    List<Diary> findTop8ByOrderByDiaryDateDesc();

    List<Diary> findTop8ByOrderByDiarySympathyCntDesc();

    Diary findByDiarySubject(String diarySubject);

    // 최신 다이어리 게시글부터 정렬
    Page<Diary> findAllByOrderByDiaryDateDesc(Pageable pageable);

    List<Diary> findAllByOrderByDiaryDateDesc();

    // 유저가 작성한 다이어리 불러오기
    List<Diary> findMyDiaryByMemberIdx(Long memberIdx);

    List<Diary> findTop6ByMemberIdxOrderByDiaryDateDesc(Long memberIdx);


    // 유저가 좋아요를 누른 다이어리 게시글 불러오기
    List<Diary> findLikeDiaryByDiaryIdx(Long diaryIdx);

    // 검색 기능 페이징에 사용되는 JPQL
    Page<Diary> findAllByDiaryWriterContaining(String diaryWriter, Pageable pageable);

    Page<Diary> findAllByDiaryContentContaining(String diaryContent, Pageable pageable);

    Page<Diary> findAllByDiaryKeywordsContaining(String diaryKeywords, Pageable pageable);

    @Query("SELECT a FROM Diary a , Member m, Sympathy s " +
            "where (m.memberIdx = :memberIdx and s.memberIdx = :memberIdx)" +
            "and s.diaryIdx = a.diaryIdx " +
            "order by a.diaryDate desc")
    List<Diary> findSympathyDiary(@Param("memberIdx") Long memberIdx);

}
