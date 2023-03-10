package com.fmi.nada.board.qna;



import java.util.List;

public interface qnaService {
    //문의 작성
    void writeQna(Qna qna);
    //리스트불러오기
    List<Qna> getList();
    //상세페이지
    Qna get(Long qna_idx);
    //수정(답변글작성)
    void answerQna(Qna qna);
    //문의삭제
    void deleteQna(Long qna_idx);




}
