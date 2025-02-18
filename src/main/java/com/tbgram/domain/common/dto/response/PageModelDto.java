package com.tbgram.domain.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageModelDto<T> {

    private List<T> results;
    private int page;
    private int pageSize;
    private Long totalElements;
}