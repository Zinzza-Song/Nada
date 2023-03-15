package com.fmi.nada.board.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * QnA Service
 */
@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public void writeQna(Qna qna) {
        qnaRepository.save(qna);
    }

    public List<Qna> getList() {
        return qnaRepository.findAll();
    }

    public Qna get(Long qnaIdx) {
        Optional<Qna> qna = qnaRepository.findById(qnaIdx);
        return qna.get();
    }

    public void answerQna(Qna qna) {
        qnaRepository.save(qna);
    }

    public void deleteQna(Long qnaIdx) {
        qnaRepository.deleteById(qnaIdx);
    }

}
