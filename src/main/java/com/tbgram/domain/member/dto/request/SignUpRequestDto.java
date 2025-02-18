package com.tbgram.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "이메일 형식에 맞게 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}",
             message = "비밀번호는 최소 8자, 영문/숫자/특수문자를 최소 1자 이상 포함해야 합니다.")
    private String password;


    @NotBlank(message = "닉네임은 필수 입력사항입니다.")
    private String nickName;

    private String introduction;

}
