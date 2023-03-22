package com.fmi.nada.diary;

import com.fmi.nada.user.Likes;
import com.fmi.nada.user.LikesRepository;
import com.fmi.nada.user.Sympathy;
import com.fmi.nada.user.SympathyRepository;
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
    private final LikesRepository likesRepository;
    private final SympathyRepository sympathyRepository;

    public Page<Diary> getDiaryList(Pageable pageable) {

        // page는 index처럼 0부터 시작
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 12);

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
    public List<Diary> findTop8ByOrderByDiaryDateDesc() {
        return diaryRepository.findTop8ByOrderByDiaryDateDesc();
    }

    // 공감 수 상위 5개의 다이어리
    public List<Diary> findTop8ByOrderByDiarySympathyCntDesc() {
        return diaryRepository.findTop8ByOrderByDiarySympathyCntDesc();
    }

    public List<Diary> findMyDiaryByMemberIdx(Long memberIdx) {
        return diaryRepository.findMyDiaryByMemberIdx(memberIdx);
    }


    public List<Diary> findSympathyDiaryList(Long memberIdx) {
        return diaryRepository.findSympathyDiary(memberIdx);
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

    // 공감 서비스 로직
    public void addSympathy(DiarySympathyDto diarySympathyDto) {
        sympathyRepository.save(new Sympathy(
                diarySympathyDto.getMemberIdx(),
                diarySympathyDto.getDiaryIdx()
        ));
    }

    // 공감 취소 서비스 로직
    public void delSympathy(DiarySympathyDto diarySympathyDto) {
        sympathyRepository.deleteByDiaryIdxAndMemberIdx(
                diarySympathyDto.getDiaryIdx(),
                diarySympathyDto.getMemberIdx()
        );
    }

    // 공감 여부 확인 서비스 로직
    public Sympathy checkSympathy(DiarySympathyDto diarySympathyDto) {
        return sympathyRepository.findByMemberIdxAndDiaryIdx(
                diarySympathyDto.getMemberIdx(),
                diarySympathyDto.getDiaryIdx()
        );
    }

    // 댓글 좋아요 서비스 로직
    public void addCommentLike(CommentLikeDto commentLikeDto) {
        likesRepository.save(new Likes(
                commentLikeDto.getMemberIdx(),
                commentLikeDto.getCommentIdx(),
                commentLikeDto.getDiaryIdx()
        ));
    }

    // 댓글 좋아요 취소 서비스 로직
    public void delCommentLike(CommentLikeDto commentLikeDto) {
        likesRepository.deleteByCommentIdxAndMemberIdxAndDiaryIdx(
                commentLikeDto.getCommentIdx(),
                commentLikeDto.getMemberIdx(),
                commentLikeDto.getDiaryIdx()
        );
    }

    // 댓글 좋아요 여부 확인 서비스 로직
    public Likes checkCommentLike(CommentLikeDto commentLikeDto) {
        return likesRepository.findByMemberIdxAndCommentIdx(
                commentLikeDto.getMemberIdx(),
                commentLikeDto.getCommentIdx()
        );
    }

    public Likes checkCommentLikeWhenRead(Long memberIdx, Long CommentIdx) {
        return likesRepository.findByMemberIdxAndCommentIdx(memberIdx, CommentIdx);
    }

    public Sympathy checkSympathyDiary(Long memberIdx, Long diaryIdx) {
        return sympathyRepository.findByMemberIdxAndDiaryIdx(memberIdx, diaryIdx);
    }

    public Diary viewAdd(Diary diary) {
        diary.setDiaryCnt(diary.getDiaryCnt() + 1L);
        return diaryRepository.save(diary);
    }

}