package com.fmi.nada.board.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 신고글 Repository
 */
@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * 신고글 찾는 쿼리
     *
     * @param reportIdx
     * @return Report(해당 신고글 객체)
     */
    Report findByReportIdx(Long reportIdx);

    /**
     * 전체 신고글 조회 쿼리
     *
     * @return Page<Report>(전체 신고글 객체가 담긴 리스트(최신순으로 정렬))
     */
    @Query("SELECT r FROM Report r ORDER BY r.reportDate DESC")
    List<Report> findAllReport();

    Page<Report> findAllByOrderByReportDateDesc(Pageable pageable);

    Page<Report> findAllByReportSubjectContaining(String reportSubject, Pageable pageable);

    Page<Report> findAllByReportWriterContaining(String reportWriter, Pageable pageable);

    Page<Report> findAllByReportContentContaining(String reportContent, Pageable pageable);


}
