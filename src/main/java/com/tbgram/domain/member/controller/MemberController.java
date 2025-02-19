package com.tbgram.domain.member.controller;

import com.tbgram.domain.common.annotation.LoginUser;
import com.tbgram.domain.member.dto.request.DeleteMemberRequestDto;
import com.tbgram.domain.member.dto.response.MemberResponseDto;
import com.tbgram.domain.member.dto.request.SignUpRequestDto;
import com.tbgram.domain.member.dto.request.UpdateMemberRequestDto;
import com.tbgram.domain.member.dto.request.UpdatePasswordRequestDto;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.dto.request.*;
import com.tbgram.domain.member.dto.response.FindEmailResponseDto;
import com.tbgram.domain.member.dto.response.MemberResponseDto;
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

    /**
     * 회원가입
     *
     * @param requestDto 회원가입 요청
     * @return 가입된 회원의 정보, 상태코드 201
     */
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

    /**
     * 회원 정보 수정
     *
     * @param requestDto 회원정보 수정(닉네임, 한줄소개) 요청
     * @param id 로그인된 사용자의 id값
     * @return 수정된 회원의 정보, 상태코드 200
     */
    @PutMapping
    public ResponseEntity<MemberResponseDto> updateMember(
            @LoginUser Long id,
            @RequestBody @Valid UpdateMemberRequestDto requestDto) {
        MemberResponseDto memberResponseDto = memberService.updateMember(
                id,
                requestDto.getNickName(),
                requestDto.getIntroduction()
        );
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * 비밀번호 수정
     *
     * @param requestDto 현재 비밀번호, 변경할 비밀번호 요청
     * @param id 로그인된 사용자의 id값
     * @return 비밀번호가 수정된 회원의 정보, 상태코드 200
     */
    @PutMapping("/password")
    public ResponseEntity<MemberResponseDto> updatePassword(
            @LoginUser Long id,
            @RequestBody UpdatePasswordRequestDto requestDto){
        MemberResponseDto memberResponseDto = memberService.updatePassword(
                id,
                requestDto.getOldPassword(),
                requestDto.getNewPassword()
        );
        return ResponseEntity.ok(memberResponseDto);
    }

    /**
     * 회원 탈퇴
     *
     * @param id 로그인된 사용자의 id값
     * @param requestDto 현재 비밀번호(검증)
     * @return 상태코드 204 No Content 나오는거 같네용.
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @LoginUser Long id,
            @RequestBody DeleteMemberRequestDto requestDto) {
        memberService.delete(id, requestDto.getPassword());
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

    /**
     * 회원 단건 조회
     *
     * @param id 조회할 회원의 id값
     * @return id값으로 조회된 회원 정보, 상태코드 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findById(@PathVariable Long id){
        MemberResponseDto memberResponseDto = memberService.findById(id);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    /**
     * 회원 단건 조회
     *
     * @param requestDto email조회를 위한 nickName 요청
     * @return nickName으로 조회된 회원의 email 반환, 상태코드 200
     */
    @GetMapping("/email")
    public ResponseEntity<FindEmailResponseDto> findEmailByNickName(@RequestBody FindEmailRequestDto requestDto){
        FindEmailResponseDto findEmailResponseDto = memberService.findByEmailByNickName(requestDto.getNickName());
        return new ResponseEntity<>(findEmailResponseDto, HttpStatus.OK);
    }

}
