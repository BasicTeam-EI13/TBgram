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

    private Long id;
    private String contents;
    private Long memberId;
    private String memberNickName;
    private Long newsFeedId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .memberId(comment.getMember().getId())
                .memberNickName(comment.getMember().getNickName())
                .newsFeedId(comment.getNewsFeed().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
