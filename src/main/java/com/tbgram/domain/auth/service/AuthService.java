package com.tbgram.domain.auth.service;

import com.tbgram.config.PasswordEncoder;
import com.tbgram.domain.auth.dto.request.SigninRequestDto;
import com.tbgram.domain.auth.dto.response.SigninResponseDto;
import com.tbgram.domain.auth.entity.Member;
import com.tbgram.domain.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    public SigninResponseDto signin(SigninRequestDto requestDto){
        Member member = authRepository.findByEmailOrElseThrow(requestDto.getEmail());

//        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
//        }

        if(!member.getPassword().equals(requestDto.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
        }

        SigninResponseDto signinResponseDto = new SigninResponseDto(member.getId(), member.getEmail(), member.getNickName());

        return signinResponseDto;
    }
}
