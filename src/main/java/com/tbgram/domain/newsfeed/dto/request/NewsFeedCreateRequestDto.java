package com.tbgram.domain.newsfeed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsFeedCreateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목은 최대 20자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

}
