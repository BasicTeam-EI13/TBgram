package com.tbgram.domain.member.controller;


import com.tbgram.domain.common.annotation.CheckAuth;
import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.common.annotation.LoginUser;
import com.tbgram.domain.member.dto.request.DeleteMemberRequestDto;
import com.tbgram.domain.member.dto.response.MemberResponseDto;
import com.tbgram.domain.member.dto.request.SignUpRequestDto;
import com.tbgram.domain.member.dto.request.UpdateMemberRequestDto;
import com.tbgram.domain.member.dto.request.UpdatePasswordRequestDto;
import com.tbgram.domain.member.dto.response.ProfileResponseDto;
import com.tbgram.domain.member.dto.request.*;
import com.tbgram.domain.member.dto.response.FindEmailResponseDto;
import com.tbgram.domain.member.service.MemberService;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;

import com.tbgram.domain.newsfeed.service.NewsFeedService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

//     private NewsFeedService newsFeedService;
  
    private final NewsFeedService newsFeedService;

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
    @CheckAuth
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
    @CheckAuth
    @PutMapping("/password")
    public ResponseEntity<MemberResponseDto> updatePassword(
            @LoginUser Long id,
            @RequestBody @Valid UpdatePasswordRequestDto requestDto){
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
     * @return 상태코드 204 No Content
     */
    @CheckAuth
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @LoginUser Long id,
            @RequestBody DeleteMemberRequestDto requestDto,
            HttpServletRequest request) {

        memberService.delete(id, requestDto.getPassword());

        // 세션 제거
        HttpSession session = request.getSession();
        session.invalidate();

        return ResponseEntity.noContent().build();
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
     * 프로필 조회
     *
     * @param member_id 조회할 회원의 id값
     * @param page 조회할 페이지 (기본값 = 1)
     * @return 회원id와 페이지 번호로 조회된 프로필, 상태코드 200
     */
    @GetMapping("/{member_id}/profile")
    public ResponseEntity<ProfileResponseDto> getMemberProfile(
            @PathVariable Long member_id,
            @RequestParam(defaultValue = "1") int page){
        // 첫 페이지 번호1, 5개씩 페이지네이션 설정
        Pageable pageable = PageRequest.of(page - 1, 5);

        // 멤버 정보 조회
        MemberResponseDto memberDto = memberService.findById(member_id); //레지스트리에서 예외처리 되어있음

        // 해당 멤버가 작성한 뉴스피드 조회
        PageModelDto<NewsFeedResponseDto> newsFeeds = newsFeedService.getMemberNewsFeeds(member_id, pageable);
        if (newsFeeds == null || newsFeeds.getResults().isEmpty()) {
            throw new IllegalArgumentException(memberDto.getNickName() + "님이 작성한 뉴스피드가 없습니다.");
        }

        // 프로필 정보에 뉴스피드 추가(병합)
        ProfileResponseDto responseDto = ProfileResponseDto.toDto(memberDto, newsFeeds.getResults());

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    /**
     * 이메일 찾기
     *
     * @param requestDto email조회를 위한 nickName 요청
     * @return nickName으로 조회된 회원의 email 반환, 상태코드 200
     */
    @CheckAuth
    @GetMapping("/email")
    public ResponseEntity<FindEmailResponseDto> findEmailByNickName(@RequestBody FindEmailRequestDto requestDto){
        FindEmailResponseDto findEmailResponseDto = memberService.findByEmailByNickName(requestDto.getNickName());
        return new ResponseEntity<>(findEmailResponseDto, HttpStatus.OK);
    }

}
