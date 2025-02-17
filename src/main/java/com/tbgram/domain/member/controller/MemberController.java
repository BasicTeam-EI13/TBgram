package com.tbgram.domain.member.controller;

import com.tbgram.domain.member.dto.MemberResponseDto;
import com.tbgram.domain.member.dto.SignUpRequestDto;
import com.tbgram.domain.member.dto.UpdateMemberRequestDto;
import com.tbgram.domain.member.dto.UpdatePasswordRequestDto;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        MemberResponseDto memberResponseDto = memberService.signUp(
                requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getNickName(),
                requestDto.getIntroduction()
        );
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }

    // 회원정보수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequestDto requestDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(
                id,
                requestDto.getNickName(),
                requestDto.getIntroduction()
        );
        return ResponseEntity.ok(memberResponseDto);
    }

    // 비밀번호수정
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequestDto requestDto){
        MemberResponseDto memberResponseDto = memberService.updatePassword(
                id,
                requestDto.getOldPassword(),
                requestDto.getNewPassword()
        );
        return ResponseEntity.ok(memberResponseDto);
    }

    // 회원탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 프로필 업데이트
    @PutMapping("/{id}/profile")
    public ResponseEntity<MemberResponseDto> updateProfile(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequestDto requestDto) {
        Member updatedMember = memberService.updateProfile(
                id, requestDto.getNickName(), requestDto.getIntroduction()
        );
        return ResponseEntity.ok(MemberResponseDto.fromEntity(updatedMember));
    }
}
