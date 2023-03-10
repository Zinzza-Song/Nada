package com.fmi.nada.board.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class qnaServiceImpl implements qnaService {

    private final qnaRepository qnaRepository;

    @Override
    public void writeQna(Qna qna) {
        qnaRepository.save(qna);
    }

    @Override
    public List<Qna> getList() {
        return qnaRepository.findAll();
    }

    @Override
    public Qna get(Long qna_idx) {
        Optional<Qna> qna = qnaRepository.findById(qna_idx);
        return qna.get();
    }

    @Override
    public void answerQna(Qna qna) {
        qnaRepository.save(qna);
    }

    @Override
    public void deleteQna(Long qna_idx) {
        qnaRepository.delete(qna_idx);
    }




}
