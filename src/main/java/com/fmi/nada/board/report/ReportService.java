package com.fmi.nada.board.report;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 신고글 Service
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    /**
     * 전체 신고글 조회 서비스 - 페이징 처리
     *
     * @return Page<Report> 전체 신고글 객체가 담긴 리스트(최신순으로 정렬)
     */
    public Page<Report> findAllByOrderByReportDateDesc(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return reportRepository.findAllByOrderByReportDateDesc(pageable);
    }
    public Page<Report> findAllByReportSubjectContaining(String reportSubject, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return reportRepository.findAllByReportSubjectContaining(reportSubject, pageable);
    }

    /**
     * 전체 신고글 조회 서비스
     *
     * @return List<Report> 전체 신고글 객체가 담긴 리스트(최신순으로 정렬)
     */
    public List<Report> findAllReport() {
        return reportRepository.findAllReport();
    }

    /**
     * 특정 신고글 조회 서비스
     *
     * @param reportIdx 조회할 신고글 PK
     * @return Report 조회할 신고글 객체
     */
    public Report findByReportIdx(Long reportIdx) {
        return reportRepository.findByReportIdx(reportIdx);
    }

    /**
     * 신고글 작성 서비스
     *
     * @param reporter       신고자
     * @param reportDto      신고글 작성 내용이 들어있는 DTO
     * @param reportedMember 신고 대상자
     */
    public void writeReport(
            Member reporter,
            ReportDto reportDto,
            Member reportedMember) {
        reportRepository.save(new Report(
                reporter.getMemberIdx(),
                reportDto.getReportSubject(),
                reporter.getUsername(),
                reportDto.getReportCategory(),
                reportDto.getReportContent(),
                reportedMember.getUsername(),
                reportDto.getReportFile()
        ));
    }

    /**
     * 신고글 처리 서비스
     *
     * @param reportIdx 처리할 신고글의 PK
     * @param reportDto 신고글 처리를 위한 DTO
     */
    public void reportPro(Long reportIdx, ReportProDto reportDto) {
        Report report = reportRepository.findByReportIdx(reportIdx);
        report.setReportIsCompleted(true);
        report.setReportSubject("[처리완료] " + report.getReportSubject());
        report.setReportAdminMent(reportDto.getReportAdminMent());

        reportRepository.save(report);
    }

}
