package com.fmi.nada.admin;

import com.fmi.nada.analytics.QuickstartJsonCredentialsSample;
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
import java.util.HashMap;
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
    public final QuickstartJsonCredentialsSample ga;

    @GetMapping
    public String adminMain(Model model) {
        try {
            HashMap<String, Integer> events = ga.getEvent(); // 발생한 이벤트와 횟수가 들어있는 해쉬맵
            events.put("처음 방문자", events.remove("first_visit"));
            events.put("사이트 이용자", events.remove("user_engagement"));
            events.remove("form_start");
            events.remove("scroll");
            events.put("페이지 로드", events.remove("page_view"));
            events.remove("session_start");
            events.remove("form_submit");
            events.remove("view_search_results"); // 이벤트 중 사용할 이벤트만 걸러냄

            HashMap<String, Integer> views = ga.getViewOfPage(); // 페이지별로 접속한 횟수가 들어있는 해쉬맵
            views.remove("");
            views.remove("(not set)");
            views.remove("일기 목록");
            views.remove("NADA 나의 다이어리");

            HashMap<String, Integer> devices = ga.getDeviceCategory(); // 접속한 기종별 횟수가 들어있는 해쉬맵
            HashMap<String, Integer> cities = ga.getCity(); // 접속한 지역과 횟수가 들어있는 해쉬맵

            // 테스트용 해쉬맵 출력
//            for (Map.Entry<String, Integer> entry : views.entrySet()) {
//                System.out.println("[Key]:" + entry.getKey() + " [Value]:" + entry.getValue());
//            }

            model.addAttribute("events", events);
            model.addAttribute("views", views);
            model.addAttribute("devices", devices);
            model.addAttribute("cities", cities);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        if (keyword == null)
            adminAllLogList = logService.findAllByOrderByLogDateDesc();
        else if (type.equals("UserID"))
            adminAllLogList = logService.findAllByLogMemberEmailContainingOrderByLogDateDesc(keyword);
        else if (type.equals("UserService"))
            adminAllLogList = logService.findAllByLogUsedServiceContainingOrderByLogDateDesc(keyword);

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
