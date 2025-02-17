package com.tbgram.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigininResponseDto {

    private Long id;
    private String email;
    private String nickName;

    public SigininResponseDto(Long id, String email, String nickName) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
    }
}
