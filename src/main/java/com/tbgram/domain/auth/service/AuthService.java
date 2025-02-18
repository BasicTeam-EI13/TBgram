package com.tbgram.domain.auth.service;

import com.tbgram.config.PasswordEncoder;
import com.tbgram.domain.auth.dto.request.SigninRequestDto;
import com.tbgram.domain.auth.dto.response.SigininResponseDto;
import com.tbgram.domain.auth.repository.AuthRepository;
import com.tbgram.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    public SigininResponseDto signin(SigninRequestDto requestDto){
        Member member = authRepository.findByEmailOrElseThrow(requestDto.getEmail());

        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
        }

        SigininResponseDto signinResponseDto = new SigininResponseDto(member.getId(), member.getEmail(), member.getNickName());

        return signinResponseDto;
    }
}
