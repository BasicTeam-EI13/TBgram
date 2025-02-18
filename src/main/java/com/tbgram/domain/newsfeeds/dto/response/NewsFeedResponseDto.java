package com.tbgram.domain.newsfeeds.dto.response;

import com.tbgram.domain.newsfeeds.entity.NewsFeed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsFeedResponseDto {

    private Long id;
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NewsFeedResponseDto fromEntity(NewsFeed newsFeed) {
        return NewsFeedResponseDto.builder()
                .id(newsFeed.getId())
                .title(newsFeed.getTitle())
                .contents(newsFeed.getContents())
                .writer(newsFeed.getMember().getNickName()) // 작성자 닉네임 반환
                .createdAt(newsFeed.getCreatedAt())
                .updatedAt(newsFeed.getUpdatedAt())
                .build();
    }
}
