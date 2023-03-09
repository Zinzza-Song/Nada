package com.fmi.nada.service;

import com.fmi.nada.entity.Test;
import com.fmi.nada.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Test saveTest(String name) {
        return testRepository.save(new Test(name));
    }
}
