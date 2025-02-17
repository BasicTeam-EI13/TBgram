package com.tbgram.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SigninRequestDto {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message="이메일을 입력해주세요")
    private final String email;
    @NotBlank(message="비밀번호를 입력해주세요")
    private final String password;

    public SigninRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
