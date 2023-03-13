package com.fmi.nada.admin;

import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final LogService logService;
    public final MemberService memberService;
    public final DiaryService diaryService;

    @GetMapping
    public String adminMain() {
        return "admin/index";
    }

    @GetMapping("/log")
    public String log(Model model) {
        List<Log> adminAllLogList = logService.findAllByOrderByLogDateDesc();
        model.addAttribute("adminAllLogList", adminAllLogList);

        return "admin/log";
    }

    @GetMapping("/user")
    public String member(Model model) {
        List<Member> adminmemberList = memberService.memberList();
        model.addAttribute("adminmemberList", adminmemberList);

        return "admin/user";
    }

    @DeleteMapping("/user_delete/{memberIdx}")
    public String deleteMember(@PathParam("memberIdx") Long memberIdx) {
        memberService.delMember(memberIdx);

        return "redirect:/user";
    }

}
