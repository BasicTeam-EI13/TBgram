package com.tbgram.domain.auth.controller;

import com.tbgram.domain.auth.consts.Consts;
import com.tbgram.domain.auth.dto.request.SigninRequestDto;
import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.auth.dto.response.SigninResponseDto;
import com.tbgram.domain.auth.service.AuthService;
import com.tbgram.domain.common.annotation.CheckAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    private ResponseEntity<SigninResponseDto> signin (@RequestBody @Valid SigninRequestDto requestDto,
                                                      HttpServletRequest request){

        SigninResponseDto responseDto = authService.signin(requestDto);
        HttpSession session = request.getSession(false);
        if(session != null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이미 로그인된 상태입니다.");
        }
        session = request.getSession();
        session.setAttribute(Consts.LOGIN_USER,
                new SessionUser(responseDto.getId(), responseDto.getEmail(), responseDto.getNickName()));

        return ResponseEntity.ok(authService.signin(requestDto));
    }

    @CheckAuth
    @PostMapping("/signout")
    private ResponseEntity<Void> signout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
