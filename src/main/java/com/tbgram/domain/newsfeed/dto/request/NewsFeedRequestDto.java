package com.tbgram.domain.newsfeed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsFeedRequestDto {
    private String title;
    private String contents;
}
