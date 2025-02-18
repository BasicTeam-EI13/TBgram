package com.tbgram.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {


    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickName;

    private String introduction;
}