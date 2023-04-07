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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String adminMain(Model model) {
        // 현재 날짜 ( DB에 저장된 날짜 임시 지정 )
        // of() 를 now()로 변경하면 된다.
        LocalDate localDate = LocalDate.of(2023, 03, 23);
        List<String> UserLogArr_add = new ArrayList<>();
        List<String> UserLogArr_del = new ArrayList<>();
        List<String> diaryCount = new ArrayList<>();
        List<String> dateArr = new ArrayList<>();

        // 다이어리 7일치 데이터 가져오기
        LocalDate maxLocalDate = localDate.plusDays(1);
        LocalDate minLocalDate = localDate.minusDays(7);

        String str_maxLocalDate = maxLocalDate.toString();
        String str_minLocalDate = minLocalDate.toString();
        String str_localDate = "";
        String str2_localDate = "";

        diaryCount.add(logService.countDiaryLogCountBy7days(str_minLocalDate, str_maxLocalDate, "일기작성"));
        diaryCount.add(logService.countDiaryLogCountBy7days(str_minLocalDate, str_maxLocalDate, "일기삭제"));
        System.out.println(diaryCount);

        // 회원가입, 회원탈퇴
        for (int i = 1; i < 8; i++) {
            UserLogArr_add.add(logService.countUserLogsCountByDate(str2_localDate, "회원가입"));
            UserLogArr_del.add(logService.countUserLogsCountByDate(str2_localDate, "회원탈퇴"));

            LocalDate new_localDate = localDate.minusDays(i);
            str_localDate = new_localDate.toString();
            dateArr.add(str_localDate);

            LocalDate new2_localDate = new_localDate.minusDays(1);
            str2_localDate = new2_localDate.toString();
            str2_localDate = "%"+str2_localDate+"%";
        }

        System.out.println(UserLogArr_add);
        System.out.println(UserLogArr_del);
        System.out.println(dateArr);

        model.addAttribute("diaryCount", diaryCount);
        model.addAttribute("UserLogArr_add", UserLogArr_add);
        model.addAttribute("UserLogArr_del", UserLogArr_del);
        model.addAttribute("dateArr", dateArr);

        return "admin/index";
    }

    /**
     * 전체 로그 조회
     *
     * @param model 전체 로그 리스트를 담을 모델
     * @return 로그 조회 뷰 페이지
     */
    @GetMapping("/log")
    public String log(@RequestParam(value = "type", required = false) String type,
                      @RequestParam(value = "keyword", required = false) String keyword,
                      Model model) {
        List<Log> adminAllLogList = null;
        Map<String, String> dateLogList = null;
        int date_count = 1;
        // 검색 관련 조건문
        if (keyword == null)
            adminAllLogList = logService.findAllByOrderByLogDateDesc();
        else if (type.equals("UserID"))
            adminAllLogList = logService.findAllByLogMemberEmailContainingOrderByLogDateDesc(keyword);
        else if (type.equals("UserService")) {
            adminAllLogList = logService.findAllByLogUsedServiceContainingOrderByLogDateDesc(keyword);
        }
        model.addAttribute("adminAllLogList", adminAllLogList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/log";
    }

    /**
     * 전체 댓글 조회
     *
     * @param model 전체 댓글 리스트를 담을 모델
     * @return 댓글 조회 뷰 페이지
     */
    @GetMapping("/comment")
    public String comment(@RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          Model model) {
        List<Comment> adminAllCommentLogList = null;
        if (keyword == null)
            adminAllCommentLogList = commentService.findAllByOrderByCommentDateDesc();
        else if (type.equals("commentContent"))
            adminAllCommentLogList = commentService.findAllByCommentContentOrderByCommentDateDesc(keyword);
        else if (type.equals("commentWriter"))
            adminAllCommentLogList = commentService.findAllByCommentWriterOrderByCommentDateDesc(keyword);

        model.addAttribute("adminAllCommentLogList", adminAllCommentLogList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
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
     * 댓글 삭제 처리
     *
     * @param commentIdx 삭제시킬 일기의 PK
     * @param model      삭제 후 갱신된 일기 리스트를 담을 모델 객체
     * @return 전체 일기 조회 뷰 페이지의 일부분 리로드
     */
    @DeleteMapping("/comment_delete")
    public String delComment(@RequestParam("commentIdx") Long commentIdx, Model model) {
        commentService.deleteByCommentIdx(commentIdx);

        List<Comment> adminAllCommentLogList = commentService.findAllByOrderByCommentDateDesc();
        model.addAttribute("adminAllCommentLogList", adminAllCommentLogList);

        return "/admin/comment :: #commentList";
    }

    /**
     * 전체 신고글 조회
     *
     * @param model 전체 신고글 리트스를 담을 모델
     * @return 신고글 조회 뷰 페이지
     */
    @GetMapping("/report")
    public String report(@RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         Model model) {
        List<Report> allReportList = null;
        if (keyword == null)
            allReportList = reportService.findAllReport();
        else if (type.equals("reportCategory"))
            allReportList = reportService.findCategoryReportList(keyword);
        else if (type.equals("reportSubject"))
            allReportList = reportService.findSubjectReportList(keyword);
        else if (type.equals("reportReportedMember"))
            allReportList = reportService.findReportedMemberReportList(keyword);

        model.addAttribute("allReportList", allReportList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/report";
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
            @Valid @ModelAttribute("reportProBean") ReportProDto reportProDto,
            Model model,
            BindingResult result) {
        if (result.hasErrors()) {
            Report report = reportService.findByReportIdx(reportIdx);
            model.addAttribute("ReportBean", report);
            return "board/report/read";
        }

        reportService.reportPro(reportIdx, reportProDto);

        return "redirect:/admin/report";
    }

    /**
     * 전체 회원 조회
     *
     * @param model 전체 회원 리스트를 담을 모델
     * @return 회원 조회 뷰 페이지
     */
    @GetMapping("/user")
    public String member(@RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         Model model) {
        List<Member> adminMemberList = null;
        if (keyword == null)
            adminMemberList = memberService.memberList();
        else if (type.equals("nickname"))
            adminMemberList = memberService.memberSearchNicknameList(keyword);
        else if (type.equals("name"))
            adminMemberList = memberService.memberSearchNameList(keyword);
        else if (type.equals("birth"))
            adminMemberList = memberService.memberSearchBirthList(keyword);

        model.addAttribute("adminMemberList", adminMemberList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/user";
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param memberIdx 탈퇴 시킬 회원의 PK
     * @return 전체 회원 조회 페이지로 리다이렉트
     */
    @DeleteMapping("/user_delete")
    public String delMember(@RequestParam("memberIdx") Long memberIdx, Model model) {
        memberService.delMember(memberIdx);

        List<Member> adminMemberList = memberService.memberList();
        model.addAttribute("adminMemberList", adminMemberList);

        return "/admin/user :: #memberList";
    }

    /**
     * 일기 삭제 처리
     *
     * @param diaryIdx 삭제시킬 일기의 PK
     * @param model    삭제 후 갱신된 일기 리스트를 담을 모델 객체
     * @return 전체 일기 조회 뷰 페이지의 일부분 리로드
     */
    @DeleteMapping("/diary_delete")
    public String delDiary(@RequestParam("diaryIdx") Long diaryIdx, Model model) {
        diaryService.deleteDiary(diaryIdx);

        List<Diary> adminAllDiaryList = diaryService.getDiaryList();
        model.addAttribute("adminAllDiaryList", adminAllDiaryList);

        return "/admin/diary :: #diaryList";
    }

    /**
     * 전체 일기 조회
     *
     * @param model 전체 일기 리스트를 담을 모델
     * @return 전체 일기 조회 뷰 페이지
     */
    @GetMapping("/diary")
    public String diary(@RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {
        List<Diary> adminAllDiaryList = null;
        if (keyword == null)
            adminAllDiaryList = diaryService.getDiaryList();
        else if (type.equals("writer"))
            adminAllDiaryList = diaryService.findDiaryWriterList(keyword);
        else if (type.equals("subject"))
            adminAllDiaryList = diaryService.findDiarySubjectList(keyword);

        model.addAttribute("adminAllDiaryList", adminAllDiaryList);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/diary";
    }

}
