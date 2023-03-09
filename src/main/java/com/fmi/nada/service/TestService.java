package com.fmi.nada.service;

import com.fmi.nada.entity.Test;
import com.fmi.nada.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    TestRepository testRepository;

    public Test set(String name) {
         return testRepository.save(new Test(name));
    }
}
