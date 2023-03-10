package com.fmi.nada.reporting;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("reporting/")
    public String index(Model mo){
        List<Report> list = reportService.getList();
        mo.addAttribute("list",list);
        return "reporting/";
    }

    @GetMapping("reporting/write")
    public String write(){
        return "reporting/write";
    }
    @PostMapping("reporting/write_pro")
    public String writeReport(@Valid @ModelAttribute ReportDto reportDto,
                              Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        Long member_idx = member.getMember_idx();

        Report report = new Report();
        report.setMember_idx(member_idx);
        report.setReport_subject(reportDto.getReport_subject());
        report.setReport_content(reportDto.getReport_content());
        reportService.WriteReport(report);
        return "reporting/";
    }

    @GetMapping("reporting/read")
    public String read(@RequestParam("report_idx") Long report_idx, Model mo){
        Report report = reportService.get(report_idx);
        mo.addAttribute("get",report);
        return "reporting/read";
    }

    @PutMapping("reporting/complete")
    public String complete(@Valid @ModelAttribute ReportDto reportDto,
                           @RequestParam("report_idx") Long report_idx){
        Report report = reportService.get(report_idx);
        report.setReport_subject("[처리완료]"+reportDto.getReport_subject());
        report.setReport_adminment(reportDto.getReport_adminment());
        report.setReport_iscompleted(true);
        reportService.CompleteReport(report);
        return "reporting/read";
    }

}
