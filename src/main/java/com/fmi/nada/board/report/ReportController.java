package com.fmi.nada.board.report;

import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/report")
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;

    @GetMapping
    public String report(Model model) {
        List<Report> allReportList = reportService.findAllByOrderByReportDateDesc();
        model.addAttribute("allReportList", allReportList);

        return "board/report/index";
    }

    @GetMapping("/read/{reportIdx}")
    public String read(@PathVariable("reportIdx") Long reportIdx, Model model) {
        Report ReportBean = reportService.findByReportIdx(reportIdx);
        model.addAttribute("ReportBean", ReportBean);

        return "board/report/read";
    }

    @GetMapping("/write")
    public String write(
            @RequestParam("reportedMemberIdx") Long reportedMemberIdx,
            @ModelAttribute("writeReportBean") ReportDto reportDto,
            Model model,
            Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);

        Member reportedMember = memberService.findByMemberIdx(reportedMemberIdx);
        model.addAttribute("reportedMember", reportedMember);

        return "board/report/write";
    }

    @PostMapping("/write_pro")
    public String writePro(
            @Valid @ModelAttribute("writeReportBean") ReportDto reportDto,
            @RequestParam("reportedMember") Member reportedMember,
            Authentication authentication,
            BindingResult result) {
        if (result.hasErrors())
            return "board/report/write";

        Member member = (Member) authentication.getPrincipal();

        reportService.writeReport(member, reportDto, reportedMember);

        return "redirect:/board/report?pageCnt=1";
    }

}
