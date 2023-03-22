package com.fmi.nada.board.report;

import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/report")
public class ReportController {

    private final ReportService reportService;
    private final MemberService memberService;

    @GetMapping
    public String report(@PageableDefault Pageable pageable,
                         @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         Model model) {

        Page<Report> allReportList = null;
        if (keyword == null) {
            allReportList = reportService.findAllByOrderByReportDateDesc(pageable);
        } else if (keyword != null) {
            allReportList = reportService.findAllByReportSubjectContaining(keyword, pageable);
        }
        model.addAttribute("allReportList", allReportList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "board/report/index";
    }

    @GetMapping("/read")
    public String read(@RequestParam("reportIdx") Long reportIdx,
                       Model model,
                       @ModelAttribute("reportProBean") ReportProDto reportProDto) {
        Report ReportBean = reportService.findByReportIdx(reportIdx);
        reportProDto.setReportAdminMent(ReportBean.getReportAdminMent());
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
        reportDto.setReportWriter(member.getMemberNickname());
        reportDto.setReportReportedMember(reportedMember.getMemberNickname());

        return "board/report/write";
    }

    @PostMapping("/write_pro")
    public String writePro(
            @Valid @ModelAttribute("writeReportBean") ReportDto reportDto,
            Authentication authentication,
            BindingResult result,
            MultipartFile file) throws Exception {
        if (result.hasErrors())
            return "board/report/write";

        Member member = (Member) authentication.getPrincipal();

        Report report = null;
        if (file == null)
            report = reportService.writeReport(member, reportDto);

        return "redirect:/board/report/read/" + report.getReportIdx() + "?page=1";
    }

}
