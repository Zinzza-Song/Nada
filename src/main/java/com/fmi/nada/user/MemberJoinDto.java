package com.fmi.nada.user;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 회원 가입용 DTO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberJoinDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$")
    @Column(name = "member_email")
    String member_email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]",
            message = "최소 하나의 문자, 숫자, 특수문자가 포함되어야 합니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Column(name = "member_password")
    String member_password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 5, message = "이름은 2자에서 5자까지 입력 가능합니다.")
    @Pattern(regexp = "[가-힣]*$", message = "이름은 한글만 입력 가능합니다.")
    String member_name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 3, max = 8, message = "닉네임은 3자에서 8자까지 입력 가능합니다.")
    String member_nickname;

    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    String member_birth;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String member_address;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0])-([0-9]{4})-([0-9]{4})$", message = "ex) 010-1234-5678")
    String member_phone;

}
