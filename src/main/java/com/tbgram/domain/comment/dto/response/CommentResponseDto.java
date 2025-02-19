package com.tbgram.domain.comment.dto.response;

import com.tbgram.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {

    private final Long id;
    private final String contents;
    private final Long memberId;
    private final String memberNickName;
    private final Long newsFeedId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .memberId(comment.getMember() != null ? comment.getMember().getId() : null) // null 방지
                .memberNickName(comment.getMember() != null ? comment.getMember().getNickName() : "탈퇴한 사용자") // null 방지
                .newsFeedId(comment.getNewsFeed() != null ? comment.getNewsFeed().getId() : null) // null 방지
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
