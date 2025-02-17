package com.tbgram.domain.newsfeed.dto.response;

import com.tbgram.domain.newsfeed.entity.NewsFeed;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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
}
