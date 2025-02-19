package com.tbgram.domain.comment.service;

import com.tbgram.domain.comment.dto.request.CreateCommentRequestDto;
import com.tbgram.domain.comment.dto.request.UpdateCommentRequestDto;
import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.comment.entity.Comment;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NewsFeedRepository newsFeedRepository;

    @Transactional
    public CommentResponseDto createComment(Long memberId, Long newsfeedId, @Valid CreateCommentRequestDto requestDto) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        // 뉴스피드 조회
        NewsFeed newsFeed = newsFeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 뉴스피드 입니다."));
        //댓글 생성
        Comment comment = Comment.builder()
                .contents(requestDto.getComment())
                .member(member)
                .newsFeed(newsFeed)
                .build();
        //댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long memberId, Long commentId, @Valid UpdateCommentRequestDto requestDto) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 본인 검증
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("본인의 댓글만 수정할 수 있습니다.");
        }

        //기존 댓글 수정
        comment.updateContent(requestDto.getNewComment());

        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 댓글입니다."));

        // 본인 검증
        if(!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("본인의 댓글만 삭제할 수 있습니다.");
        }

        //댓글 삭제
        commentRepository.delete(comment);
    }

    //특정 뉴스피드의 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByNewsFeed(Long newsFeedId) {
        List<Comment> comments = commentRepository.findByNewsFeedIdOrderByCreatedAtDesc(newsFeedId);
        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }


}
