package com.tbgram.domain.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponseDto {

    private final Long id;
    private final String email;
    private final String nickName;
}
