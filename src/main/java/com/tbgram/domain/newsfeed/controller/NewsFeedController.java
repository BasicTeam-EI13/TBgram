package com.tbgram.domain.newsfeed.controller;

import com.tbgram.domain.newsfeed.dto.response.NewsPageResponseDto;
import org.springframework.web.bind.annotation.RestController;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.service.NewsFeedService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/newsfeeds")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    //뉴스피드 작성
    @PostMapping("/{memberId}")
    public ResponseEntity<NewsFeedResponseDto> createNewsFeed(
            @PathVariable Long memberId,
            @RequestBody NewsFeedRequestDto requestDto)
    {
        return ResponseEntity.ok(newsFeedService.createNewsFeed(memberId, requestDto));
    }


    //뉴스피드 전체 조회 (최신순, 페이징)
    @GetMapping
    public ResponseEntity<NewsPageResponseDto<NewsFeedResponseDto>> getAllNewsFeeds(Pageable pageable) {
        return ResponseEntity.ok(newsFeedService.getAllNewsFeeds(pageable));
    }


    //뉴스피드 상세 조회
    @GetMapping("/{newsfeedId}")
    public ResponseEntity<NewsFeedResponseDto> getNewsFeed(@PathVariable Long newsfeedId) {
        return ResponseEntity.ok(newsFeedService.getNewsFeed(newsfeedId));
    }


    //뉴스피드 수정
    @PutMapping("/{newsfeedId}/{memberId}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
            @PathVariable Long newsfeedId,
            @PathVariable Long memberId,
            @RequestBody NewsFeedRequestDto requestDto) {
        return ResponseEntity.ok(newsFeedService.updateNewsFeed(newsfeedId, memberId, requestDto));
    }


    //뉴스피드 삭제
    @DeleteMapping("/{newsfeedId}/{memberId}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable Long newsfeedId,
            @PathVariable Long memberId) {
        newsFeedService.deleteNewsFeed(newsfeedId, memberId);
        return ResponseEntity.noContent().build();

    }
}
