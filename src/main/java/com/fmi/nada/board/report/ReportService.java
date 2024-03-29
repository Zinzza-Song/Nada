package com.fmi.nada.board.report;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
        return reportRepository.findAllByReportSubjectContainingOrderByReportDateDesc(reportSubject, pageable);
    }

    public Page<Report> findAllByReportWriterContaining(String reportWriter, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return reportRepository.findAllByReportWriterContainingOrderByReportDateDesc(reportWriter, pageable);
    }

    public Page<Report> findAllByReportContentContaining(String reportContent, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);
        return reportRepository.findAllByReportContentContainingOrderByReportDateDesc(reportContent, pageable);
    }

    /**
     * 전체 신고글 조회 서비스
     *
     * @return List<Report> 전체 신고글 객체가 담긴 리스트(최신순으로 정렬)
     */
    public List<Report> findAllReport() {
        return reportRepository.findAllReport();
    }

    public List<Report> findCategoryReportList(String category){
        return reportRepository.findCategoryReportList(category);
    }

    public List<Report> findSubjectReportList(String subject){
        return reportRepository.findSubjectReportList(subject);
    }

    public List<Report> findReportedMemberReportList(String reportedMember) {
        return reportRepository.findReportedMemberReportList(reportedMember);
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
     * @param reporter  신고자
     * @param reportDto 신고글 작성 내용이 들어있는 DTO
     * @return 작성된 Report 객체
     */
    public Report writeReport(
            Member reporter,
            ReportDto reportDto) {
        return reportRepository.save(new Report(
                reporter.getMemberIdx(),
                reportDto.getReportSubject(),
                reportDto.getReportWriter(),
                reportDto.getReportCategory(),
                reportDto.getReportContent(),
                reportDto.getReportReportedMember()
        ));
    }

    public Report writeReportFile(ReportDto reportDto, Member reporter, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir");
        String getFullPath = projectPath + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(getFullPath, fileName);
        Report report = new Report();
        report.setMemberIdx(reporter.getMemberIdx());
        report.setReportSubject(reportDto.getReportSubject());
        report.setReportWriter(reportDto.getReportWriter());
        report.setReportContent(reportDto.getReportContent());
        report.setReportCategory(reportDto.getReportCategory());
        report.setReportReportedMember(reportDto.getReportReportedMember());
        if (file.getOriginalFilename() == null && file.getOriginalFilename() == "") {
            return reportRepository.save(report);
        } else {
            if (!saveFile.exists()) {
                saveFile.mkdirs();
                file.transferTo(saveFile);
                report.setReportFile(fileName);

                return reportRepository.save(report);
            } else {
                file.transferTo(saveFile);
                report.setReportFile(fileName);

                return reportRepository.save(report);
            }
        }
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

    public void addReportView(Report report) {
        reportRepository.save(report);
    }

}