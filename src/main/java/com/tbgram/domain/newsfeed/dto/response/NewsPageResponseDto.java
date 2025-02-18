package com.tbgram.domain.newsfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NewsPageResponseDto<T> {
    private List<T> content;  // 데이터 리스트
    private int pageNumber;   // 현재 페이지 번호
    private int totalPages;   // 전체 페이지 개수
    private long totalElements; // 전체 데이터 개수

}