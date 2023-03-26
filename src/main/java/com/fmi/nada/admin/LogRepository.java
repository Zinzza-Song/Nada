package com.fmi.nada.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByOrderByLogDateDesc();

}
