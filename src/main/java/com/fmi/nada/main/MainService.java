package com.fmi.nada.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 메인 페이지 서비스
 */
@Service
@RequiredArgsConstructor
public class MainService {

    private final AdviceRepository adviceRepository;

    public List<Advice> findByAll() {
        return adviceRepository.findByAll();
    }

}
