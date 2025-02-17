package com.tbgram.domain.member.dto;

import com.tbgram.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String email;
    private String nickName;
    private String introduction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .introduction(member.getIntroduction())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
