package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/join")
public class MemberController {

    private final MemberService memberService;
    private final MailAuthService mailAuthService;

    @GetMapping
    public String join(@ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto) {
        return "user/join";
    }

    @GetMapping("/email_exist_check")
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

    @PostMapping("/join_pro")
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

}
