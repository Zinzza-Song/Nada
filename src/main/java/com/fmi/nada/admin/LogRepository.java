package com.fmi.nada.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository  extends JpaRepository<Log, Long> {

    List<Log> findAllByOrderByLogDateDesc();

}
