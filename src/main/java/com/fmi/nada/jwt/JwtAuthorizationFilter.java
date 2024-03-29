package com.fmi.nada.jwt;

import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * JWT를 이용한 인증
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthorizationFilter(MemberRepository userRepository, RefreshTokenService refreshTokenService) {
        this.memberRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;

        // cookie에서 JWT token을 가져옴
        try {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception e) {
        }

        if (token != null) {
            try {
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                String userName = token = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("userName"))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);

                System.out.println(userName);
                RefreshToken refreshToken = refreshTokenService.getRefreshToken(userName);

//                System.out.println(refreshToken.getUserName());

                if (refreshToken != null) {
                    token = JwtUtils.createToken(memberRepository.findByUsername(userName));

                    Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token);
                    cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키 만료시간 설정
                    cookie.setPath("/");
                    response.addCookie(cookie);

                    Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환
     * User가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        String userName = JwtUtils.getUsername(token);
        if (userName != null) {
            Member member = memberRepository.findByUsername(userName); // 유저명으로 유저를 DB에서 찾음

            return new UsernamePasswordAuthenticationToken(
                    member,
                    null,
                    member.getAuthorities()
            );
        }

        return null;
    }

}
