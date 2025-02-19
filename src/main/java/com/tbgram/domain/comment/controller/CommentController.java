package com.tbgram.domain.comment.controller;


import com.tbgram.domain.auth.consts.Consts;
import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.comment.dto.request.CreateCommentRequestDto;
import com.tbgram.domain.comment.dto.request.UpdateCommentRequestDto;
import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.comment.service.CommentService;
import com.tbgram.domain.common.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     *
     * @param newsFeedId
     * @param requestDto
     * @param userId
     * @return
     */
    @PostMapping("/{news_feed_id}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable("news_feed_id") Long newsFeedId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            @LoginUser Long userId) {

        CommentResponseDto responseDto = commentService.createComment(userId, newsFeedId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 수정
     *
     * @param commentId
     * @param requestDto
     * @param userId
     * @return
     */
    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            @LoginUser Long userId) {

        CommentResponseDto responseDto = commentService.updateComment(userId, commentId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @param userId
     * @return
     */
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @PathVariable("comment_id") Long commentId,
            @LoginUser Long userId) {
         commentService.deleteComment(userId, commentId);
         return ResponseEntity.noContent().build();
     }

    // 특정 뉴스피드의 댓글 목록 조회
    @GetMapping("/news-feeds/{newsfeed_id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNewsFeed(@PathVariable("newsfeed_id") Long newsFeedId) {
        List<CommentResponseDto> responseDtoList = commentService.getCommentsByNewsFeed(newsFeedId);
        return ResponseEntity.ok(responseDtoList);
    }
}
