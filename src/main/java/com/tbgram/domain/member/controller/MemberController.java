package com.tbgram.domain.member.controller;


import com.tbgram.domain.member.dto.request.DeleteMemberRequestDto;
import com.tbgram.domain.member.dto.response.MemberResponseDto;
import com.tbgram.domain.member.dto.request.SignUpRequestDto;
import com.tbgram.domain.member.dto.request.UpdateMemberRequestDto;
import com.tbgram.domain.member.dto.request.UpdatePasswordRequestDto;
import com.tbgram.domain.member.dto.response.ProfileResponseDto;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.service.MemberService;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsPageResponseDto;
import com.tbgram.domain.newsfeed.service.NewsFeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final NewsFeedService newsFeedService;

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
    @PutMapping("/{id}/password")
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
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestBody DeleteMemberRequestDto requestDto) {
        memberService.delete(id, requestDto.getPassword());
        return ResponseEntity.noContent().build();
    }

    // 단건조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findById(@PathVariable Long id){
        MemberResponseDto memberResponseDto = memberService.findById(id);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // 프로필 조회
    @GetMapping("{member_id}/profile")
    public ResponseEntity<ProfileResponseDto> getMemberProfile(@PathVariable Long member_id, Pageable pageable){
        // 멤버 정보 조회
        MemberResponseDto memberDto = memberService.findById(member_id);

        // 해당 멤버가 작성한 뉴스피드 조회
        NewsPageResponseDto<NewsFeedResponseDto> newsFeeds = newsFeedService.getMemberNewsFeeds(member_id, pageable);
        List<NewsFeedResponseDto> newsFeedList = new ArrayList<>(newsFeeds.getContent());

        // 프로필 정보에 뉴스피드 추가(병합)
        ProfileResponseDto responseDto = ProfileResponseDto.toDto(memberDto, newsFeedList);

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}
