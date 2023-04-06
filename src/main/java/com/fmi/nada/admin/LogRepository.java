package com.fmi.nada.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT COUNT(l) FROM Log l WHERE CAST(l.logDate AS string) LIKE :logDate AND l.logUsedService = :logUsedService")
    String countLogsDate(@Param("logDate") String logDate, @Param("logUsedService") String logUsedService);

    @Query("SELECT COUNT(l) FROM Log l WHERE CAST(l.logDate AS string) >= :minLogDate AND CAST(l.logDate AS string) <= :maxLogDate AND l.logUsedService = :logUsedService")
    String countDiaryLog(@Param("minLogDate") String minLogDate, @Param("maxLogDate") String maxLogDate, @Param("logUsedService") String logUsedService);

}
