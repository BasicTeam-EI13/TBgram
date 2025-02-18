package com.tbgram.domain.newsfeed.dto.response;

import com.tbgram.domain.newsfeed.entity.NewsFeed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NewsFeedResponseDto {
    private Long id;
    private String authorNickname; // ✅ 작성자 닉네임 추가
    private String title;
    private String contents;
    private LocalDateTime createdAt;

    public NewsFeedResponseDto(NewsFeed newsFeed) {
        this.id = newsFeed.getId();
        this.authorNickname = newsFeed.getMember().getNickName(); // ✅ 작성자 닉네임 반영
        this.title = newsFeed.getTitle();
        this.contents = newsFeed.getContents();
        this.createdAt = newsFeed.getCreatedAt();
    }

    public static NewsFeedResponseDto fromEntity(NewsFeed newsFeed) {
        return NewsFeedResponseDto.builder()
                .id(newsFeed.getId())
                .authorNickname(newsFeed.getMember().getNickName())
                .title(newsFeed.getTitle())
                .contents(newsFeed.getContents())
                .createdAt(newsFeed.getCreatedAt())
                .build();
    }

}
