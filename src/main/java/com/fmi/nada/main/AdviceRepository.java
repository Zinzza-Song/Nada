package com.fmi.nada.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 오늘의 조언 레포지토리
 */
public interface AdviceRepository extends JpaRepository<Advice, Long> {

    @Query(value = "SELECT * FROM advice order by RAND() limit 1", nativeQuery = true)
    List<Advice> findByAll();

}
