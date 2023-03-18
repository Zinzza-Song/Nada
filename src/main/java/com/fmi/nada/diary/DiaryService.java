package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Diary Service
 */
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public Page<Diary> getDiaryList(Pageable pageable) {

        // page는 index처럼 0부터 시작
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 6);

        return diaryRepository.findAllByOrderByDiaryDateDesc(pageable);
    }

    public List<Diary> getDiaryList() {
        return diaryRepository.findAllByOrderByDiaryDateDesc();
    }

    public List<Diary> findTop6ByMemberIdxOrderByDiaryDateDesc(Long memberIdx) {
        return diaryRepository.findTop6ByMemberIdxOrderByDiaryDateDesc(memberIdx);
    }

    public Diary registerDiary(Long memberIdx,
                               String diarySubject,
                               String diaryWriter,
                               String diaryContent,
                               String diaryKeywords,
                               String diaryAnalyze,
                               Boolean diaryPublicable,
                               Boolean diary_analyzePublicable) {
        Diary diary = new Diary(memberIdx,
                diarySubject,
                diaryWriter,
                diaryContent,
                diaryKeywords,
                diaryAnalyze,
                diaryPublicable,
                diary_analyzePublicable);
        diary.setDiaryCnt(1L);
        diary.setDiarySympathyCnt(0L);
        return diaryRepository.save(diary);
    }

    // 다이어리 메인 게시판에서 보여지는 다이어리 리스트
    public Diary findByDiaryIdx(Long diaryIdx) {
        return diaryRepository.findByDiaryIdx(diaryIdx);
    }

    // 다이어리 수정
    public void modifyDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    // 다이어리 등록 시 등록된 다이어리의 정보를 사용하기 위한 메서드
    public Diary findByDiary_subject(String diarySubject) {
        return diaryRepository.findByDiarySubject(diarySubject);
    }

    // 최슨 작성된 5개의 다이어리
    public List<Diary> findTop5ByOrderByDiaryDateDesc() {
        return diaryRepository.findTop5ByOrderByDiaryDateDesc();
    }

    // 공감 수 상위 5개의 다이어리
    public List<Diary> findTop5ByOrderByDiarySympathyCntDesc() {
        return diaryRepository.findTop5ByOrderByDiarySympathyCntDesc();
    }

    public List<Diary> findMyDiaryByMemberIdx(Long memberIdx) {
        return diaryRepository.findMyDiaryByMemberIdx(memberIdx);
    }

    public List<Diary> getLikeDiary(Long diaryIdx) {
        return diaryRepository.findLikeDiaryByDiaryIdx(diaryIdx);
    }

    // 다이어리 상세 보기
    public Diary getDiaryDetail(Long diaryIndex) {
        return diaryRepository.getById(diaryIndex);
    }

    public void deleteDiary(Long diaryIdx) {
        diaryRepository.deleteById(diaryIdx);
    }

    // 다이어리 검색 페이징
    public Page<Diary> findAllByDiaryWriterContaining(String diaryWriter, Pageable pageable) {
        // page는 index처럼 0부터 시작
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 6);

        return diaryRepository.findAllByDiaryWriterContaining(diaryWriter, pageable);
    }

    public Page<Diary> findAllByDiaryContentContaining(String diaryContent, Pageable pageable) {
        // page는 index처럼 0부터 시작
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 6);

        return diaryRepository.findAllByDiaryContentContaining(diaryContent, pageable);
    }

    public Page<Diary> findAllByDiaryKeywordsContaining(String diaryKeywords, Pageable pageable) {
        // page는 index처럼 0부터 시작
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 6);

        return diaryRepository.findAllByDiaryKeywordsContaining(diaryKeywords, pageable);
    }
}