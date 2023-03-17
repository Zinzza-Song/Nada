package com.fmi.nada.board.qna;

import com.fmi.nada.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * QnA Repository
 */
public interface QnaRepository extends JpaRepository<Qna,Long> {


}
