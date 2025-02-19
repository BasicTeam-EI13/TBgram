package com.tbgram.domain.comment.controller;


import com.tbgram.domain.auth.consts.Consts;
import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.comment.dto.request.CreateCommentRequestDto;
import com.tbgram.domain.comment.dto.request.UpdateCommentRequestDto;
import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     *
     * @param newsfeed_id
     * @param requestDto
     * @param request
     * @return
     */
    @PostMapping("/news-feeds/{newsfeed_id}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable("newsfeed_id") Long newsfeed_id,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        CommentResponseDto responseDto = commentService.createComment(sessionUser.getId(), newsfeed_id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 수정
     *
     * @param newsfeed_id
     * @param comment_id
     * @param requestDto
     * @param request
     * @return
     */
    @PutMapping("/news-feeds/{newsfeed_id}/comments/{comment_id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("newsfeed_id") Long newsfeed_id,
            @PathVariable("comment_id") Long comment_id,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        CommentResponseDto responseDto = commentService.updateComment(sessionUser.getId(), comment_id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 삭제
     *
     * @param newsfeed_id
     * @param comment_id
     * @param request
     * @return
     */
    @DeleteMapping("/news-feeds/{newsfeed_id}/comments/{comment_id}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @PathVariable("newsfeed_id") Long newsfeedId,
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        commentService.deleteComment(sessionUser.getId(), commentId);
        return ResponseEntity.noContent().build();
    }

    // 특정 뉴스피드의 댓글 목록 조회
    @GetMapping("/news-feeds/{newsfeed_id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNewsFeed(@PathVariable("newsfeed_id") Long newsfeedId) {
        List<CommentResponseDto> responseDtoList = commentService.getCommentsByNewsFeed(newsfeedId);
        return ResponseEntity.ok(responseDtoList);
    }



}
