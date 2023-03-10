package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(
            String member_email,
            String member_password,
            String member_name,
            String member_nickname,
            String member_birth,
            String member_address,
            String member_phone
    ) {
        return memberRepository.save(new Member(member_email,
                passwordEncoder.encode(member_password),
                "ROLE_USER",
                member_name,
                member_nickname,
                member_birth,
                member_address,
                member_phone));
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

}
