package com.tbgram.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요")
//    @JsonProperty("newContents")
    private String newContents;
}
