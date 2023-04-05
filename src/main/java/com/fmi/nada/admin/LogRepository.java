package com.fmi.nada.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 어드민 레포지토리
 */
@Transactional
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByOrderByLogDateDesc();

    List<Log> findAllByLogMemberEmailContainingOrderByLogDateDesc(String keyword);

    List<Log> findAllByLogUsedServiceContainingOrderByLogDateDesc(String keyword);

}
