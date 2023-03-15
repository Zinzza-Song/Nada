package com.fmi.nada.board.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 신고글 Repository
 */
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
     * @return List<Report>(전체 신고글 객체가 담긴 리스트(최신순으로 정렬))
     */
    List<Report> findAllByOrderByReportDateDesc();

}
