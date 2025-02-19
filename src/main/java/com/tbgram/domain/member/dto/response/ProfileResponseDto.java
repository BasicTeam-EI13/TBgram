package com.tbgram.domain.member.dto.response;


import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProfileResponseDto {

    private final Long id;
    private final String nickname;
    private final String introduction;
    private final List<NewsFeedResponseDto> newsFeeds;

    public static ProfileResponseDto toDto(MemberResponseDto memberDto, List<NewsFeedResponseDto> newsFeeds) {
        return ProfileResponseDto.builder()
                .id(memberDto.getId())
                .nickname(memberDto.getNickName())
                .introduction(memberDto.getIntroduction())
                .newsFeeds(newsFeeds != null ? newsFeeds : List.of()) // null이면 빈리스트로 초기화
                .build();
    }
}
