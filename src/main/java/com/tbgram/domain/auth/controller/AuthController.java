package com.tbgram.domain.auth.controller;

import com.tbgram.domain.auth.consts.Consts;
import com.tbgram.domain.auth.dto.request.SigninRequestDto;
import com.tbgram.domain.auth.dto.response.SigninResponseDto;
import com.tbgram.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    private ResponseEntity<SigninResponseDto> signin (@RequestBody @Valid SigninRequestDto requestDto,
                                                      HttpServletRequest request){

        SigninResponseDto responseDto = authService.signin(requestDto);
        HttpSession session = request.getSession();
        session.setAttribute(Consts.LOGIN_USER, responseDto);

        return new ResponseEntity<>(authService.signin(requestDto), HttpStatus.OK);
    }
}
