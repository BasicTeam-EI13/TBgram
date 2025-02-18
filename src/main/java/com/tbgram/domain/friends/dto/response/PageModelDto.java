package com.tbgram.domain.friends.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageModelDto<T> {
    // 임시로 사용되는 Dto입니다.
    private List<T> results;
    private int page;
    private int pageSize;
    private Long totalElements;
}
