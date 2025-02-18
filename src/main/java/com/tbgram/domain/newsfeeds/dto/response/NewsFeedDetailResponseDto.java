package com.tbgram.domain.newsfeeds.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tbgram.domain.comments.dto.response.CommentResponseDto;
import com.tbgram.domain.newsfeeds.entity.NewsFeed;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonPropertyOrder({ "id", "title", "contents", "writer", "createdAt", "updatedAt", "comments" }) // 필드 순서 지정!
public class NewsFeedDetailResponseDto {

    private Long id;
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponseDto> comments;

    public NewsFeedDetailResponseDto(NewsFeed newsFeed, List<CommentResponseDto> comments) {
        this.id = newsFeed.getId();
        this.title = newsFeed.getTitle();
        this.contents = newsFeed.getContents();
        this.writer = newsFeed.getMember().getNickName();
        this.createdAt = newsFeed.getCreatedAt();
        this.updatedAt = newsFeed.getUpdatedAt();
        this.comments = comments;
    }
}
