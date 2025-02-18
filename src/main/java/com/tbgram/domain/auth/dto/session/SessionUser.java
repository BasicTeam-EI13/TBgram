package com.tbgram.domain.auth.dto.session;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionUser {
    private Long id;
    private String email;
    private String nickName;
}
