package com.tbgram.domain.newsfeed.controller;

import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedCreateRequestDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedUpdateRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedDetailResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.service.NewsFeedService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
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
            HttpServletRequest request,
            @RequestBody @Valid NewsFeedCreateRequestDto requestDto) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long memberId = ((SessionUser) session.getAttribute("loginUser")).getId();

        NewsFeedResponseDto responseDto = newsFeedService.createNewsFeed(memberId, requestDto);
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
    public ResponseEntity<NewsFeedDetailResponseDto> getNewsFeedDetail(@PathVariable Long newsFeedId) {
        NewsFeedDetailResponseDto responseDto = newsFeedService.getNewsFeedDetail(newsFeedId);
        return ResponseEntity.ok(responseDto);
    }

    // 뉴스피드 수정
    @PutMapping("/{news_feeds_id}")
    public ResponseEntity<NewsFeedResponseDto> updateNewsFeed(
            @PathVariable Long newsFeedId,
            HttpServletRequest request,
            @RequestBody @Valid NewsFeedUpdateRequestDto requestDto) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = ((SessionUser) session.getAttribute("loginUser")).getId();

        NewsFeedResponseDto responseDto = newsFeedService.updateNewsFeed(newsFeedId, memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 뉴스피드 삭제
    @DeleteMapping("/{news_feeds_id}")
    public ResponseEntity<Void> deleteNewsFeed(
            @PathVariable Long newsFeedId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = ((SessionUser) session.getAttribute("loginUser")).getId();

        newsFeedService.deleteNewsFeed(newsFeedId, memberId);
        return ResponseEntity.noContent().build();
    }

    // 친구들의 뉴스피드 조회 (최신순, 페이징)
    @GetMapping("/friends")
    public ResponseEntity<PageModelDto<NewsFeedResponseDto>> getFriendsNewsFeeds(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long memberId = ((SessionUser) session.getAttribute("loginUser")).getId();

        PageModelDto<NewsFeedResponseDto> responseDto = newsFeedService.getFriendNewsFeeds(memberId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/friends/{friendId}")
    public ResponseEntity<PageModelDto<NewsFeedResponseDto>> getSpecificFriendNewsFeeds(
            @PathVariable Long friendId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Long memberId = ((SessionUser) session.getAttribute("loginUser")).getId();

        PageModelDto<NewsFeedResponseDto> responseDto = newsFeedService.getSpecificFriendNewsFeeds(memberId, friendId, page, size);

        return ResponseEntity.ok(responseDto);
    }
}

