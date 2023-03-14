package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 회원 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(
            String username,
            String password,
            String memberName,
            String memberNickname,
            String memberBirth,
            String memberAddress,
            String memberPhone
    ) {
        return memberRepository.save(new Member(username,
                passwordEncoder.encode(password),
                "ROLE_USER",
                memberName,
                memberNickname,
                memberBirth,
                memberAddress,
                memberPhone));
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);    }



    public List<Member> memberList() {
        return memberRepository.findAllByOrderByMemberJoinDateDesc();
    }

    public void delMember(Long memberIdx) {
        memberRepository.deleteMemberByMemberIdx(memberIdx);
    }

    public void updatePw(Member member) {
        memberRepository.save(member);
    }

}
