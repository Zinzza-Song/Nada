package com.fmi.nada.reporting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public void WriteReport(Report report){
        reportRepository.save(report);
    }

    public List<Report> getList(){
        return reportRepository.findAll();
    }

    public Report get(Long report_idx){
        Optional<Report> byId = reportRepository.findById(report_idx);
        return byId.get();
    }

    public void CompleteReport(Report report){
        reportRepository.save(report);
    }

}
