package com.fmi.nada.config;

import com.fmi.nada.admin.Log;
import com.fmi.nada.admin.LogRepository;
import com.fmi.nada.jwt.JwtAuthenticationFilter;
import com.fmi.nada.jwt.JwtAuthorizationFilter;
import com.fmi.nada.jwt.JwtProperties;
import com.fmi.nada.jwt.JwtUtils;
import com.fmi.nada.user.Member;
import com.fmi.nada.user.MemberRepository;
import com.fmi.nada.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.Cookie;
import java.util.Arrays;

/**
 * Security 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();
        http.rememberMe().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager(), logRepository),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(memberRepository),
                BasicAuthenticationFilter.class
        );

        http.authorizeRequests()
                .antMatchers("/", "/home", "/user/join/**", "/user/reset_password/**",
                        "/diary", "/diary/read", "/board/notice", "/board/notice/read",
                        "/user/login", "/denied").permitAll()
                .antMatchers("/user/modify/**", "/user/delete", "/user/read",
                        "/user/friend_list", "/user/friend_add", "/user/friend_del", "/user/block_list",
                        "/user/unblock", "/reporting/**", "/diary/**", "board/QNA/**", "board/report/**").hasRole("USER")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated();
//                .anyRequest().permitAll();

        http.formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .addLogoutHandler(((request, response, authentication) -> {
                    String token = "";
                    try {
                        token = Arrays.stream(request.getCookies())
                                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME))
                                .findFirst()
                                .map(Cookie::getValue)
                                .orElse(null);
                    } catch (Exception ignored) {
                    }
                    String userName = JwtUtils.getUsername(token);
                    Member member = memberRepository.findByUsername(userName);
                    logRepository.save(new Log(
                            member.getMemberIdx(),
                            true,
                            member.getUsername(),
                            "로그아웃"
                    ));
                }))
                .logoutSuccessHandler(((request, response, authentication) -> {
                    response.sendRedirect("/user/login");
                }))
                .deleteCookies(JwtProperties.COOKIE_NAME);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적 리소스 spring security 대상에서 제외 web.ignoring().antMatchers("/images/**", "/css/**"); 아래 코드와 같은 내용
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * UserDetailsService 구현
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            Member member = memberService.findByUsername(username);
            if (member == null)
                throw new UsernameNotFoundException(username);

            return member;
        };
    }

    /**
     * 접근 거부 예외처리 구현
     *
     * @return accessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler("/denied");

        return accessDeniedHandler;
    }

}