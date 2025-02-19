package com.tbgram.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequestDto {

    @NotEmpty(message = "댓글 내용을 작성해주세요.")
    private String contents;
}
