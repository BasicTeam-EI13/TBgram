package com.tbgram.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdatePasswordRequestDto {

    @NotBlank(message = "기존 비밀번호를 입력하세요.")
    private String oldPassword;

    @NotBlank(message = "변경할 비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}",
            message = "비밀번호는 최소 8자, 영문/숫자/특수문자를 최소 1자 이상 포함해야 합니다.")
    private String newPassword;
}
