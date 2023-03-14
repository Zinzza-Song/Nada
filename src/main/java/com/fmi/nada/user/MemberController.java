package com.fmi.nada.user;

import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/")
public class MemberController {

    private final MemberService memberService;
    private final MailAuthService mailAuthService;
    private final PasswordEncoder passwordEncoder;

    private final DiaryService diaryService;

    @GetMapping("/join")
    public String join(@ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto) {
        return "user/join";
    }

    //회원가입 이메일인증
    @GetMapping("/join/email_exist_check")
    @ResponseBody
    public String mailCheck(String username) throws Exception {
        Member member = memberService.findByUsername(username);
        if (member != null && member.getUsername().equals(username)) {
            return "false";
        } else {
            System.out.println("이메일 인증 요청이 들어옴!");
            System.out.println("이메일 인증 이메일 : " + username);
            return mailAuthService.sendSimpleMessage(username);
        }
    }

    //비밀번호 찾기 이메일인증
    @GetMapping("/join/email_exist_check_pw")
    @ResponseBody
    public String mailCheckPw(String username, String memberName) throws Exception {
        Member member = memberService.findByUsername(username);
        if (member != null && member.getUsername().equals(username) && member.getMemberName().equals(memberName)) {
            System.out.println("이메일 인증 요청이 들어옴!");
            System.out.println("이메일 인증 이메일 : " + username);
            System.out.println("이메일 인증 유저 : " + memberName);
            return mailAuthService.sendSimpleMessage(username);
        } else {
            return "false";
        }
    }

    @PostMapping("/join/join_pro")
    public String join_pro(
            @Valid @ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto,
            BindingResult result) {
        if (result.hasErrors())
            return "user/join";

        memberService.join(
                memberJoinDto.getUsername(),
                memberJoinDto.getPassword(),
                memberJoinDto.getMemberName(),
                memberJoinDto.getMemberNickname(),
                memberJoinDto.getMemberBirth(),
                memberJoinDto.getMemberAddress(),
                memberJoinDto.getMemberPhone());

        return "redirect:/";
    }

    //비밀번호 찾기페이지 경로매핑
    @GetMapping("/find_password")
    public String findPw() {
        return "user/find_password";
    }

    //비밀번호 변경페이지 경로매핑
    @GetMapping("/reset_password")
    public String resetPassword(@RequestParam("username") String username, Model mo) {
        mo.addAttribute("username", username);
        return "user/reset_password";
    }

    //비밀번호 변경 서비스 수행 로직
    @PutMapping("/reset_password")
    public String resetPassword(
            @ModelAttribute @Valid FindPasswordDto findPasswordDto,
            @RequestParam("username") String username) {
        Member member = memberService.findByUsername(username);
        member.setPassword(passwordEncoder.encode(findPasswordDto.getPassword()));
        memberService.updatePw(member);
        return "redirect:/";
    }

    @GetMapping("/read/{memberIdx}")
    public String readMember(@PathVariable("memberIdx") Long memberIdx,
                            Authentication authentication,
                            Model model) {
        List<Diary> myList = diaryService.findMyDiaryBymemberIdx(memberIdx);
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("memberLoginBean",member);
        model.addAttribute("myList",myList);
        return "user/read";
    }

    @GetMapping("/read")
    public String myPage(){
        return "user/read";
    }
}
