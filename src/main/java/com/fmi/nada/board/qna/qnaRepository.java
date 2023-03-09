package com.fmi.nada.board.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface qnaRepository extends JpaRepository<Qna,Long> {

    void delete(Long qna_idx);
}
