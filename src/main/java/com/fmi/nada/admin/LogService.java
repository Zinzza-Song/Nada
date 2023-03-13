package com.fmi.nada.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    List<Log> findAllByOrderByLogDateDesc() {
        return logRepository.findAllByOrderByLogDateDesc();
    }

}
