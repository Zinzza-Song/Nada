package com.fmi.nada.board.report;

import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

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
        } else if (type.equals("reportWriter")) {
            allReportList = reportService.findAllByReportWriterContaining(keyword, pageable);
        } else if (type.equals("reportContent")) {
            allReportList = reportService.findAllByReportContentContaining(keyword, pageable);
        } else if (type.equals("reportSubject")) {
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
        ReportBean.setReportView(ReportBean.getReportView() + 1L);
        reportService.addReportView(ReportBean);
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
        if (file.isEmpty())
            report = reportService.writeReport(member, reportDto);
        else {
            report = reportService.writeReportFile(reportDto, member, file);
        }

        return "redirect:/board/report/read?reportIdx=" + report.getReportIdx() + "&page=1";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("reportIdx") Long reportIdx) throws Exception {

        Report report = reportService.findByReportIdx(reportIdx);
        String projectPath = System.getProperty("user.dir");
        String getFullPath = projectPath + "\\src\\main\\resources\\static\\files";
        UrlResource resource = new UrlResource("file:" + getFullPath + "\\" + report.getReportFile());
        String encodedFileName = UriUtils.encode(report.getReportFile(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }

}