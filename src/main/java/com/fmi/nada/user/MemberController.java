package com.fmi.nada.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.nada.diary.Analyzed;
import com.fmi.nada.diary.AnalyzedService;
import com.fmi.nada.diary.Diary;
import com.fmi.nada.diary.DiaryService;
import com.fmi.nada.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final MailAuthService mailAuthService;
    private final PasswordEncoder passwordEncoder;
    private final DiaryService diaryService;
    private final AnalyzedService analyzedService;

//    @GetMapping("/loginfail")
//    public String loginFail() {
//        return "user/loginfail";
//    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "errorMsg", required = false) String errorMsg,
            Model model) {
        model.addAttribute("error", error);
        model.addAttribute("errorMsg", errorMsg);

        return "user/login";
    }

    @GetMapping("/join")
    public String join(@ModelAttribute("memberJoinBean") MemberJoinDto memberJoinDto) {
        return "user/join";
    }

    @GetMapping("join/nickname_exist_check")
    @ResponseBody
    public String nicknameCheck(String memberNickname) {
        Member member = memberService.findByMemberNickname(memberNickname);

        if(member != null)
            return "no";
        else
            return "ok";
    }

    //회원가입 이메일인증
    @GetMapping("/join/email_exist_check")
    @ResponseBody
    public String mailCheck(String username) throws Exception {
        Member member = memberService.findByUsername(username);
        if (member != null) {
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
//        && member.getMemberName().equals(memberName)
        if (member != null && member.getUsername().equals(username) ) {
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

    @GetMapping("/read")
    public String readMember(@ModelAttribute("memberLoginBean") MemberJoinDto memberJoinDto,
                             Authentication authentication,
                             Model model) throws Exception {
        Member member = (Member) authentication.getPrincipal();

        model.addAttribute("memberLoginBean", member);

        Long memberIdx = member.getMemberIdx();

        // 작성한 다이어리 점수 10, 20, 30,... 문자열로 만들어 model에 담기
        String analyzeScore = "";
        List<Analyzed> diaryScore = analyzedService.findTop6ByDiaryMemberIdx(memberIdx);
        for (int index = 0; index < diaryScore.size(); index++) {
            analyzeScore += diaryScore.get(index).getAnalyzeScore() + ",";
        }
        analyzeScore = analyzeScore.substring(0, analyzeScore.length() - 1);
        model.addAttribute("analyzeScore", analyzeScore);

        // 작성한 다이어리 분석 점수 그래프에 띄우기
        List<Integer> analyzeScoreArr = new ArrayList<>();

        for (int i = 0; i < diaryScore.size(); i++) {
            analyzeScoreArr.add(diaryScore.get(i).getAnalyzeScore());
        }
        model.addAttribute("analyzeScoreArr", analyzeScoreArr);

        // 작성한 다이어리의 작성일을 그래프에 띄우기
        List<Diary> getDiaryWriteDate = diaryService.findTop6ByMemberIdxOrderByDiaryDateDesc(memberIdx);
        List<String> diaryWriteDateArr = new ArrayList<>();

        for (int i = 0; i < getDiaryWriteDate.size(); i++) {
            diaryWriteDateArr.add(getDiaryWriteDate.get(i).getDiaryDate().toString());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String diaryWriteDateJson = objectMapper.writeValueAsString(diaryWriteDateArr);
        model.addAttribute("diaryWriteDateArr", diaryWriteDateJson);

        // 공감이 있는 글인지 아닌지 여부에 따라 분리
        Sympathy sympathy = memberService.getLikeIdx(memberIdx);
        if (sympathy == null) {
            List<Diary> myDiaryList = diaryService.findTop6ByMemberIdxOrderByDiaryDateDesc(memberIdx);
            model.addAttribute("myDiaryList", myDiaryList);

            List<Friends> friendsList = memberService.friendsList(memberIdx);
            model.addAttribute("friendsList", friendsList);

            List<BlockList> blockLists = memberService.blockLists(memberIdx);
            model.addAttribute("blockLists", blockLists);

        } else {
            Long likeDiaryIdx = sympathy.getDiaryIdx();
            System.out.println(likeDiaryIdx);

            List<Diary> myDiaryList = diaryService.findMyDiaryByMemberIdx(memberIdx);
            model.addAttribute("myDiaryList", myDiaryList);

            List<Friends> friendsList = memberService.friendsList(memberIdx);
            model.addAttribute("friendsList", friendsList);

            List<BlockList> blockLists = memberService.blockLists(memberIdx);
            model.addAttribute("blockLists", blockLists);

            List<Diary> likeDiaryList = diaryService.getLikeDiary(likeDiaryIdx);
            model.addAttribute("likeDiaryList", likeDiaryList);

        }
        return "user/read";
    }

    @GetMapping("/modify")
    public String modify(@ModelAttribute("memberInfoBean") MemberJoinDto memberJoinDto,
                         Authentication authentication, Model model) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("memberLoginBean", member);
        return "user/modify";
    }

    @PutMapping("/modify_pro")
    public String modify_pro(@Valid @ModelAttribute("memberInfoBean") MemberUpdateDto memberUpdateDto,
                             @ModelAttribute("memberLoginBean") MemberJoinDto memberJoinDto,
                             Authentication authentication, BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("memberLoginBean", member);
            return "user/read";
        } else {
            Member member = (Member) authentication.getPrincipal();
            member.setMemberNickname(memberUpdateDto.getMemberNickname());
            member.setPassword(passwordEncoder.encode(memberUpdateDto.getPassword()));
            member.setMemberAddress(memberUpdateDto.getMemberAddress());
            member.setMemberPhone(memberUpdateDto.getMemberPhone());
            memberService.updateMember(member);
            model.addAttribute("memberLoginBean", member);
        }
        return "user/read";
    }

    @DeleteMapping("/delete")
    public String deleteMember(@RequestParam("memberIdx") Long memberIdx, HttpServletResponse res) {
        memberService.delMember(memberIdx);

        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return "redirect:/";

    }

    @PostMapping("/friend_add")
    @ResponseBody
    public String addFriend(FriendsDto friendsDto, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        Member friendMember = memberService.findByMemberIdx(friendsDto.getFriendsIdx());

        List<Friends> friends = memberService.findFriendsByMemberIdxAndFriendsMemberIdx(member, friendMember);
        if(!friends.isEmpty())
            return "already";

        BlockList blockList = memberService.findByBlockMemberIdxAndMemberIdx(
                friendMember.getMemberIdx(),
                member.getMemberIdx());
        if(blockList == null) {
            memberService.addFriends(member, friendMember);
            return "ok";
        }

        return "fail";
    }

    @DeleteMapping("/friend_del")
    public String delFriend(MemberDeleteDto memberDeleteDto,
                            Authentication authentication,
                            Model model) {
        Member member=(Member)authentication.getPrincipal();

        memberService.delFriends(member.getMemberIdx(), memberDeleteDto.getFriendsIdx());
        List<Friends> friendsList= memberService.friendsList(member.getMemberIdx());
        model.addAttribute("friendsList",friendsList);

        return "/user/read :: #friendsList";
    }

    @PostMapping("/blockList_add")
    @ResponseBody
    public String addBlockList(
            BlockListDto blockListDto,
            Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        memberService.addBlockList(member, blockListDto);

        return "ok";
    }

}