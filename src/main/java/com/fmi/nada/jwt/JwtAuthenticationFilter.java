package com.fmi.nada.jwt;

import com.fmi.nada.admin.Log;
import com.fmi.nada.admin.LogRepository;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * JWT를 이용한 로그인 인증
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final LogRepository logRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            LogRepository logRepository,
            MemberRepository memberRepository,
            RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.logRepository = logRepository;
        this.memberRepository = memberRepository;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * 로그인 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
                new ArrayList<>()
        );

        System.out.println("토큰 인증");

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 인증에 성공했을 때 사용
     * JWt Token을 생성해서 쿠키에 삽입
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     *                   method.
     * @throws IOException
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException {
        Member user = (Member) authResult.getPrincipal();
        String token = JwtUtils.createToken(user);

        logRepository.save(new Log(
                user.getMemberIdx(),
                true,
                user.getUsername(),
                "로그인"
        ));

        user.setMemberLoginDate(LocalDateTime.now());
        memberRepository.save(user);

        //쿠키 생성
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token);
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키 만료시간 설정
        cookie.setPath("/");

        Cookie userNameCookie = new Cookie("userName", user.getUsername());
        userNameCookie.setMaxAge(JwtProperties.EXPIRATION_REFRESH_TIME);
        userNameCookie.setPath("/");

        RefreshToken refreshToken = refreshTokenService.getRefreshToken(user.getUsername());

        if (refreshToken == null) {
            refreshTokenService.createRefreshToken(user);
            System.out.println("리프레시 토큰 생성");
        } else
            System.out.println(refreshToken.getUserName());


        response.addCookie(cookie);
        response.addCookie(userNameCookie);
        response.sendRedirect("/");
    }

    /**
     * 인증에 실패했을 때 사용
     * login 페이지로 리다이랙트
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed)
            throws IOException {

        System.out.println("인증 실패");

        String errorMessage = "이메일 혹은 비밀번호를 잘못 입력하였습니다.";
        if (failed instanceof BadCredentialsException)
            errorMessage = "이메일 혹은 비밀번호를 잘못 입력하였습니다.";
        else if (failed instanceof InsufficientAuthenticationException)
            errorMessage = "Invalid Secret Key";

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        response.sendRedirect("/user/login?error=true&errorMsg=" + errorMessage);
    }

}

