package com.fmi.nada.board.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * QnA Repository
 */
@Transactional
public interface QnaRepository extends JpaRepository<Qna, Long> {

    Page<Qna> findAllByOrderByQnaDateDesc(Pageable pageable);

    Page<Qna> findAllByQnaWriterContainingOrderByQnaDateDesc(String keyword, Pageable pageable);

    Page<Qna> findAllByQnaSubjectContainingOrderByQnaDateDesc(String keyword, Pageable pageable);

    Page<Qna> findAllByQnaContentContainingOrderByQnaDateDesc(String keyword, Pageable pageable);
}
