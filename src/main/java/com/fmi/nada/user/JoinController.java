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
    public String join(@ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto) {
        return "user/join";
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
                memberJoinDto.getMember_name(),
                memberJoinDto.getMember_nickname(),
                memberJoinDto.getMember_birth(),
                memberJoinDto.getMember_address(),
                memberJoinDto.getMember_phone());

        return "redirect:/";
    }

}
