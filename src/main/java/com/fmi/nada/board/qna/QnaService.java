package com.fmi.nada.board.qna;

import com.fmi.nada.diary.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * QnA Service
 */
@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;


    public void writeQnaFile(Qna qna, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir");
        String getFullPath = projectPath + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(getFullPath,fileName);
        if(file.getOriginalFilename()==null && file.getOriginalFilename()==""){
            qnaRepository.save(qna);
        }else {
            if (!saveFile.exists()) {
                saveFile.mkdirs();
                file.transferTo(saveFile);
                qna.setQnaFile(fileName);

                qnaRepository.save(qna);
            } else {
                file.transferTo(saveFile);
                qna.setQnaFile(fileName);

                qnaRepository.save(qna);
            }
        }
    }
    public void writeQna(Qna qna){
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
