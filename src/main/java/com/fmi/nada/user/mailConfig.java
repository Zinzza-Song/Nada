package com.fmi.nada.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class mailConfig {

    @Bean
    public JavaMailSender javaMailSender(){

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        // 호스트(네이버 사용) 네이버 메일 - > 환경설정 -> POP3/IMAP 설정 -> POP3/SMTP 설정 -> 하단의 SMTP 서버명
        javaMailSender.setUsername("skycindy89@naver.com");
        // 서버 열어놓은 계정 입력
        javaMailSender.setPassword("XTK2C46XJU8Y");
        // 서버 열어놓은 계정의 어플리케이션 비밀번호 입력
        javaMailSender.setPort(465);
        // 네이버 SMTP 포트 465로 고정되어 있음
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();

        properties.setProperty("mail.transport.protocol", "smtp");
        // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true");
        // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true");
        // smtp strattles 사용
        properties.setProperty("mail.debug", "true");
        // 디버그 사용
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
        // ssl 인증 서버는 smtp.naver.com
        properties.setProperty("mail.smtp.ssl.enable","true");
        // ssl 사용
        return properties;
    }

}
