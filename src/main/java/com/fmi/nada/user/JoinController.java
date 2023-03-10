package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/join")
public class JoinController {

    private final MemberService memberService;

    @GetMapping
    public String join() {
        return "user/join";
    }

    @PostMapping("/join_pro")
    public String join_pro(
            @Valid @ModelAttribute MemberJoinDto memberJoinDto,
            BindingResult result) {
        if (result.hasErrors())
            return "user/join";

        memberService.join(memberJoinDto.getMember_email(),
                memberJoinDto.member_password,
                memberJoinDto.member_name,
                memberJoinDto.member_nickname,
                memberJoinDto.member_birth,
                memberJoinDto.member_address,
                memberJoinDto.member_phone);

        return "redirect:/user/login";
    }

}
