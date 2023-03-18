package com.fmi.nada.admin;

import com.fmi.nada.board.report.Report;
import com.fmi.nada.board.report.ReportProDto;
import com.fmi.nada.board.report.ReportService;
import com.fmi.nada.diary.Comment;
import com.fmi.nada.diary.CommentService;
import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 관리자 Controller
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final LogService logService;
    public final MemberService memberService;
    public final DiaryService diaryService;
    public final CommentService commentService;
    public final ReportService reportService;

    @GetMapping
    public String adminMain() {
        return "admin/index";
    }

    /**
     * 전체 로그 조회
     *
     * @param model 전체 로그 리스트를 담을 모델
     * @return 로그 조회 뷰 페이지
     */
    @GetMapping("/log")
    public String log(Model model) {
        List<Log> adminAllLogList = logService.findAllByOrderByLogDateDesc();
        model.addAttribute("adminAllLogList", adminAllLogList);

        return "admin/log";
    }

    /**
     * 전체 댓글 조회
     *
     * @param model 전체 댓글 리스트를 담을 모델
     * @return 댓글 조회 뷰 페이지
     */
    @GetMapping("/comment")
    public String comment(Model model) {
        List<Comment> adminAllCommentLogList = commentService.findAllByOrderByCommentDateDesc();
        model.addAttribute("adminAllCommentLogList", adminAllCommentLogList);

        return "admin/comment";
    }

    /**
     * 댓글 삭제
     *
     * @param commentIdx 삭제할 댓글의 PK
     * @return 댓글 조회 페이지로 리다이렉트
     */
    @DeleteMapping("comment_delete/{commentIdx}")
    public String delComment(@PathParam("commentIdx") Long commentIdx) {
        commentService.deleteByCommentIdx(commentIdx);

        return "redirect:/comment";
    }

    /**
     * 전체 신고글 조회
     *
     * @param model 전체 신고글 리트스를 담을 모델
     * @return 신고글 조회 뷰 페이지
     */
    @GetMapping("/report")
    public String report(Model model) {
        List<Report> allReportList = reportService.findAllByOrderByReportDateDesc();
        model.addAttribute("allReportList", allReportList);

        return "admin/report";
    }

    @GetMapping("/report_pro/{reportIdx}")
    public String reportPro(@PathVariable("reportIdx") Long reportIdx,
                            @ModelAttribute ReportProDto reportProDto) {


        return "admin/repotPro";
    }

    /**
     * 신고 처리
     *
     * @param reportIdx    신고 처리한 신고글의 PK
     * @param reportProDto 신고글 업데이트에 사용할 DTO
     * @return 해당 신고글 조회 페이지로 리다이렉트
     */
    @PutMapping("/report_pro/{reportIdx}")
    public String reportPro(
            @PathVariable("reportIdx") Long reportIdx,
            @Valid @ModelAttribute ReportProDto reportProDto,
            BindingResult result) {
        if (result.hasErrors())
            return "board/report/read";

        reportService.reportPro(reportIdx, reportProDto);

        return "redirect:/board/report/read/" + reportIdx;
    }

    /**
     * 전체 회원 조회
     *
     * @param model 전체 회원 리스트를 담을 모델
     * @return 회원 조회 뷰 페이지
     */
    @GetMapping("/user")
    public String member(Model model) {
        List<Member> adminMemberList = memberService.memberList();
        model.addAttribute("adminMemberList", adminMemberList);

        return "admin/user";
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param memberIdx 탈퇴 시킬 회원의 PK
     * @return 전체 회원 조회 페이지로 리다이렉트
     */
    @DeleteMapping("/user_delete/{memberIdx}")
    public String delMember(@PathVariable("memberIdx") Long memberIdx) {
        memberService.delMember(memberIdx);

        return "redirect:/user";
    }

    /**
     * 전체 일기 조회
     *
     * @param model 전체 일기 리스트를 담을 모델
     * @return 전체 일기 조회 뷰 페이지
     */
    @GetMapping("/diary")
    public String diary(Model model) {
        List<Diary> adminAllDiaryList = diaryService.getDiaryList();
        model.addAttribute("adminAllDiaryList ", adminAllDiaryList);

        return "admin/diary";
    }

    /**
     * 일기 삭제 처리
     *
     * @param diaryIdx 삭제시킬 일기의 PK
     * @return 전체 일기 조회 뷰 페이지로 리다이렉트
     */
    @DeleteMapping("/diary_delete/{diaryIdx}")
    public String delDiary(@PathVariable("diaryIdx") Long diaryIdx) {
        diaryService.deleteDiary(diaryIdx);

        return "redirect:/diary";
    }

}
