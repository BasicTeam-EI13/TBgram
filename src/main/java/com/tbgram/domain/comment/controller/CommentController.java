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
            @PathVariable Long newsfeedId,
            @RequestBody @Valid CreateCommentRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        CommentResponseDto responseDto = commentService.createComment(sessionUser.getId(), newsfeedId, requestDto);
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
            @PathVariable Long newsfeedId,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequestDto requestDto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        CommentResponseDto responseDto = commentService.updateComment(sessionUser.getId(), commentId, requestDto);
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
            @PathVariable Long newsfeedId,
            @PathVariable Long commentId,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (SessionUser) session.getAttribute(Consts.LOGIN_USER);
        commentService.deleteComment(sessionUser.getId(), commentId);
        return ResponseEntity.noContent().build();
    }
}
