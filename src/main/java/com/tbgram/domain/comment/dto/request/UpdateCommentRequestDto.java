package com.tbgram.domain.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentRequestDto {

    private String commentId;
    private String newComment;
}
