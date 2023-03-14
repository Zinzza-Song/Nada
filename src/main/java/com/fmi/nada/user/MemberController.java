package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/")
public class MemberController {

    private final MemberService memberService;
    private final MailAuthService mailAuthService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String join(@ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto) {
        return "user/join";
    }

    //회원가입 이메일인증
    @GetMapping("/join/email_exist_check")
    @ResponseBody
    public String mailCheck(String username) throws Exception {
        Member member= memberService.findByUsername(username);
        if (member!=null && member.getUsername().equals(username)){
            return "false";
        }else {
            System.out.println("이메일 인증 요청이 들어옴!");
            System.out.println("이메일 인증 이메일 : " + username);
            return mailAuthService.sendSimpleMessage(username);
        }
    }

    //비밀번호 찾기 이메일인증
    @GetMapping("/join/email_exist_check_pw")
    @ResponseBody
    public String mailCheckPw(String username, String memberName) throws Exception {
        Member member= memberService.findByUsername(username);
        if (member!=null && member.getUsername().equals(username) && member.getMemberName().equals(memberName)){
            System.out.println("이메일 인증 요청이 들어옴!");
            System.out.println("이메일 인증 이메일 : " + username);
            System.out.println("이메일 인증 유저 : " + memberName);
            return mailAuthService.sendSimpleMessage(username);
        }else {
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

    //비밀번호 찾기 경로
    @GetMapping("/find_password")
    public String findPw(){
        return "user/find_password";
    }

    //비밀번호 변경 경로
    @GetMapping("/reset_password")
    public String resetPassword(@RequestParam("username") String username, Model mo){
        mo.addAttribute("username",username);
        return "user/reset_password";
    }

    //비밀번호 변경 서비스 수행 로직
    @PutMapping("/reset_password")
    public String resetPassword(@ModelAttribute MemberJoinDto memberJoinDto){
        Member member = memberService.findByUsername(memberJoinDto.getUsername());
        member.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
        memberService.updatePw(member);
        return "redirect:/";
    }

}
