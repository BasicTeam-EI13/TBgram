package com.tbgram.domain.member.service;

import com.tbgram.config.PasswordEncoder;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.member.dto.response.FindEmailResponseDto;
import com.tbgram.domain.member.dto.response.MemberResponseDto;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NewsFeedRepository newsFeedRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public MemberResponseDto signUp(String email, String password, String nickName, String introduction) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");
        }
        if (memberRepository.findByNickName(nickName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member(email, encodedPassword, nickName, introduction);
        memberRepository.save(member);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, String nickName, String introduction) {
        if (memberRepository.findByNickName(nickName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다.");
        }
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        member.updateMember(nickName, introduction);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public MemberResponseDto updatePassword(Long id, String oldPassword, String newPassword) {
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        if (oldPassword.equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "동일한 비밀번호로 변경할 수 없습니다.");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedNewPassword);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public void delete(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        //회원이 작성한 뉴스피드 조회
        List<NewsFeed> newsFeeds = newsFeedRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

        //회원이 작성한 뉴스피드에 달린 댓글 삭제
        for (NewsFeed newsFeed : newsFeeds) {
            commentRepository.deleteByNewsFeedId(newsFeed.getId());
        }

        //회원이 작성한 뉴스피드 삭제
        newsFeedRepository.deleteByMemberId(memberId);

        //회원이 작성한 댓글 삭제
        commentRepository.deleteByMemberId(memberId);

        // 회원 삭제
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id) {
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getIntroduction(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public FindEmailResponseDto findByEmailByNickName(String nickName) {
        Member member = memberRepository.findEmailByNickName(nickName);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 닉네임이 존재하지 않습니다.");
        }
        return new FindEmailResponseDto(member.getEmail());


    }

}