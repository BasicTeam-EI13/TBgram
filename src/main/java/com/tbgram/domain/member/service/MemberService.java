package com.tbgram.domain.member.service;

import com.tbgram.config.PasswordEncoder;
import com.tbgram.domain.member.dto.MemberResponseDto;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDto signUp(String email, String password, String nickName, String introduction) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member(email, encodedPassword, nickName, introduction);
        memberRepository.save(member);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, String nickName, String introduction) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.updateMember(nickName, introduction);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public MemberResponseDto updatePassword(Long id, String oldPassword, String newPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        if(!passwordEncoder.matches(oldPassword, member.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호 불일치");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedNewPassword);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.delete();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getIntroduction(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

    @Transactional
    public Member updateProfile(Long memberId,String nickName, String introduction ) {
        Member member = memberRepository.findById(memberId).get();
        member.updateProfile(nickName, introduction);
        return memberRepository.save(member);
    }
}