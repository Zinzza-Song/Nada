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
        if(keyword == null) {
            allReportList = reportService.findAllByOrderByReportDateDesc(pageable);
        }
        else if(keyword != null) {
            allReportList = reportService.findAllByReportSubjectContaining(keyword, pageable);
        }
        model.addAttribute("allReportList", allReportList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "board/report/index";
    }

    @GetMapping("/read/{reportIdx}")
    public String read(@PathVariable("reportIdx") Long reportIdx,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model,
                       @ModelAttribute("reportProBean") ReportProDto reportProDto) {
        Report ReportBean = reportService.findByReportIdx(reportIdx);
        reportProDto.setReportAdminMent(ReportBean.getReportAdminMent());
        viewCountValidation(ReportBean, request, response);
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

    private void viewCountValidation(Report report, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        // request에 쿠키들이 있을 때
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            // postView 쿠키가 있을 때
            if (cookies[i].getName().equals("noticeView")) {
                // cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + report.getReportIdx() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    report.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + report.getReportDate() + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 noticeView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            report.addViewCount();
            cookie = new Cookie("noticeView", "[" + report.getReportIdx() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
