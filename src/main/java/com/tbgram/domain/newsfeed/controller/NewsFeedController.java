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
@RequestMapping("/api/newsfeed")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    //뉴스피드 작성
    @PostMapping
    public ResponseEntity<NewsFeedResponseDto> createNewsFeed(
            @RequestParam Long memberId,
            @RequestBody NewsFeedRequestDto requestDto) {
        return ResponseEntity.ok(newsFeedService.createNewsFeed(memberId, requestDto));
    }

    //뉴스피드 전체 조회 (최신순, 페이징)
    @GetMapping
    public ResponseEntity<NewsPageResponseDto<NewsFeedResponseDto>> getAllNewsFeeds(Pageable pageable) {
        return ResponseEntity.ok(newsFeedService.getAllNewsFeeds(pageable));
    }


    //뉴스피드 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<NewsFeedResponseDto> getNewsFeed(@PathVariable Long id) {
        return ResponseEntity.ok(newsFeedService.getNewsFeed(id));
    }

    //뉴스피드 수정
    @PutMapping("/{id}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
            @PathVariable Long id,
            @RequestParam Long memberId,
            @RequestBody NewsFeedRequestDto requestDto) {
        return ResponseEntity.ok(newsFeedService.updateNewsFeed(id, memberId, requestDto));
    }

    //뉴스피드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable Long id,
            @RequestParam Long memberId) {
        newsFeedService.deleteNewsFeed(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
