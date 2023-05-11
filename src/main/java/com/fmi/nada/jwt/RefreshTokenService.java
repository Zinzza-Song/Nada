package com.fmi.nada.jwt;

import com.fmi.nada.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String userName) {
        if (refreshTokenRepository.findByUserName(userName).isPresent())
            return refreshTokenRepository.findByUserName(userName).get();
        else
            return null;
    }

    public void createRefreshToken(Member member) {
        String refreshToken = JwtUtils.createRefreshToken();
        refreshTokenRepository.save(new RefreshToken(member.getMemberIdx(), refreshToken, member.getUsername()));
    }

    public void deleteRefreshToken(Long memberIdx) {
        refreshTokenRepository.deleteById(memberIdx);
    }

}
