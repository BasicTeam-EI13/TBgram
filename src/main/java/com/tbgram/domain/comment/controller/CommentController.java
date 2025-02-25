package com.tbgram.domain.comment.controller;

import com.tbgram.domain.comment.dto.request.CreateCommentRequestDto;
import com.tbgram.domain.comment.dto.request.UpdateCommentRequestDto;
import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.comment.service.CommentService;
import com.tbgram.domain.common.annotation.CheckAuth;
import com.tbgram.domain.common.annotation.LoginUser;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     *
     * @param newsFeedId
     * @param requestDto
     * @param userId
     * @return
     */
    @CheckAuth
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
    @CheckAuth
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
    @CheckAuth
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("comment_id") Long commentId,
            @LoginUser Long userId) {
         commentService.deleteComment(userId, commentId);
         return ResponseEntity.noContent().build();
     }

}
