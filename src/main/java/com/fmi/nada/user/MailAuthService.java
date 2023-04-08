package com.fmi.nada.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
@RequestScope
public class MailAuthService {


    private final JavaMailSender javaMailSender;

    private final String ePw = createKey();

    @Value("${mail.mail-id}")
    private String mailId;

    //회원가입 인증 메일작성
    public MimeMessage createMessageJoin(String to) throws MessagingException, UnsupportedEncodingException {
        System.out.println("메일받을 사용자" + to);
        System.out.println("인증번호" + ePw);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("[NADA] 회원가입을 위한 이메일 인증코드 입니다.");

        String msgg = "";
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>나만의 감성다이어리 NADA 입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>아래 인증코드를 회원가입 페이지에 입력해주세요</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>회원가입 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>"; // 메일에 인증번호 ePw 넣기
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        //발송할 메시지, 캐릭터셋, 타입
        message.setFrom(new InternetAddress(mailId, "NADA_Admin"));
        // 발송하는 사람의 메일주소, 이름
        System.out.println("createmessage 에서 생성된 msgg 메시지" + msgg);
        System.out.println("createmessage 에서 생성된 리턴 메시지" + message);

        return message;

    }

    //비밀번호 찾기 인증메일 작성
    public MimeMessage createMessageFindPassword(String to) throws MessagingException, UnsupportedEncodingException {
        System.out.println("메일받을 사용자" + to);
        System.out.println("인증번호" + ePw);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("[NADA] 비밀번호 찾기를 위한 이메일 인증코드 입니다.");

        String msgg = "";
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>나만의 감성다이어리 NADA 입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>아래 인증코드를 비밀번호 찾기 페이지에 입력해주세요</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>비밀번호 찾기 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>"; // 메일에 인증번호 ePw 넣기
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");
        //발송할 메시지, 캐릭터셋, 타입
        message.setFrom(new InternetAddress(mailId, "NADA_Admin"));
        // 발송하는 사람의 메일주소, 이름
        System.out.println("createmessage 에서 생성된 msgg 메시지" + msgg);
        System.out.println("createmessage 에서 생성된 리턴 메시지" + message);

        return message;

    }

    //인증번호 생성
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            key.append(rnd.nextInt(10));
            //key에 랜덤 6자리 0~9까지의 난수를 담는다
        }
        return key.toString();
    }

    //회원가입 메일 발송
    public String sendSimpleMessageJoin(String to) throws Exception {
        //변수로 들어온 to는 메일주소가된다(받을주소)
        //MimeMessage 객체 안에 내가 전송할 메일 주소를 넣음
        MimeMessage message = createMessageJoin(to);
        // 메일발송
        try { // 예외처리
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증코드를 서버로 반환(비교하기위해서)
    }

    //비밀번호 찾기 메일 발송
    public String sendSimpleMessageFindPassword(String to) throws Exception {
        //변수로 들어온 to는 메일주소가된다(받을주소)
        //MimeMessage 객체 안에 내가 전송할 메일 주소를 넣음
        MimeMessage message = createMessageFindPassword(to);
        // 메일발송
        try { // 예외처리
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증코드를 서버로 반환(비교하기위해서)
    }

}