package com.tbgram.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequestDto {

    @NotEmpty(message = "댓글 내용을 작성해주세요.")
//    @JsonProperty("contents")
    private String contents;
}
