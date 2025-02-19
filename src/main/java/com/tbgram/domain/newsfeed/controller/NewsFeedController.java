package com.tbgram.domain.newsfeed.controller;

import com.tbgram.domain.common.annotation.LoginUser;
import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedCreateRequestDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedUpdateRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedDetailResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.service.NewsFeedService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/news_feeds")
@RequiredArgsConstructor
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    // 뉴스피드 작성
    @PostMapping
    public ResponseEntity<NewsFeedResponseDto> createNewsFeed(
            @LoginUser Long userId,
            @RequestBody @Valid NewsFeedCreateRequestDto requestDto) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        NewsFeedResponseDto responseDto = newsFeedService.createNewsFeed(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 전체 뉴스피드 조회 (페이징, 최신순)
    @GetMapping
    public ResponseEntity<PageModelDto<NewsFeedResponseDto>> getAllNewsFeeds(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageModelDto<NewsFeedResponseDto> responseDto = newsFeedService.getAllNewsFeeds(page, size);
        return ResponseEntity.ok(responseDto);
    }

    // 뉴스피드 상세 조회 (댓글 포함)
    @GetMapping("/{news_feeds_id}")
    public ResponseEntity<NewsFeedDetailResponseDto> getNewsFeedDetail(
            @PathVariable(name = "news_feeds_id") Long newsFeedId) {
        NewsFeedDetailResponseDto responseDto = newsFeedService.getNewsFeedDetail(newsFeedId);
        return ResponseEntity.ok(responseDto);
    }

    // 뉴스피드 수정
    @PutMapping("/{news_feeds_id}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
            @PathVariable("news_feeds_id") Long newsFeedId,
            @LoginUser Long userId,
            @RequestBody @Valid NewsFeedUpdateRequestDto requestDto) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        NewsFeedResponseDto responseDto = newsFeedService.updateNewsFeed(newsFeedId, userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 뉴스피드 삭제
    @DeleteMapping("/{news_feeds_id}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable("news_feeds_id") Long newsFeedId,
            @LoginUser Long userId) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        newsFeedService.deleteNewsFeed(newsFeedId, userId);
        return ResponseEntity.noContent().build();
    }
    // 친구들의 뉴스피드 조회 (최신순, 페이징)
    @GetMapping("/friends")
    public ResponseEntity<PageModelDto<NewsFeedResponseDto>> getFriendsNewsFeeds(
            @LoginUser Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        PageModelDto<NewsFeedResponseDto> responseDto = newsFeedService.getFriendNewsFeeds(userId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 친구의 뉴스피드 조회 (로그인 필요)
    @GetMapping("/friends/{friendId}")
    public ResponseEntity<PageModelDto<NewsFeedResponseDto>> getSpecificFriendNewsFeeds(
            @PathVariable Long friendId,
            @LoginUser Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        PageModelDto<NewsFeedResponseDto> responseDto = newsFeedService.getSpecificFriendNewsFeeds(userId, friendId, page, size);
        return ResponseEntity.ok(responseDto);
    }

}

