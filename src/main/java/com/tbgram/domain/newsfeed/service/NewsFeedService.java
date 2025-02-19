package com.tbgram.domain.newsfeed.service;

import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.friends.respository.FriendsRepository;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedCreateRequestDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedUpdateRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedDetailResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.auth.exception.ApiException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;
    private final MemberRepository memberRepository;
    private final FriendsRepository friendsRepository;
    private final CommentRepository commentRepository;

    private static final String NEWSFEED_NOT_FOUND_MSG = "존재하지 않는 뉴스피드입니다.";

    // 뉴스피드 작성
    public NewsFeedResponseDto createNewsFeed(Long memberId, NewsFeedCreateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 회원입니다."));
        NewsFeed newsFeed = NewsFeed.builder()
                .member(member)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .build();
        newsFeedRepository.save(newsFeed);
        return NewsFeedResponseDto.fromEntity(newsFeed);
    }

    // 전체 뉴스피드 조회 (최신순, 페이징)
    public PageModelDto<NewsFeedResponseDto> getAllNewsFeeds(int page, int size) {
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page - 1, size));
        return createPageResponse(newsFeedPage);
    }

    // 뉴스피드 상세 조회 (댓글 포함)
    public NewsFeedDetailResponseDto getNewsFeedDetail(Long newsFeedId) {
        NewsFeed newsFeed = getExistingNewsFeed(newsFeedId);
        List<CommentResponseDto> comments = commentRepository.findByNewsFeedIdOrderByCreatedAtDesc(newsFeedId)
                .stream().map(CommentResponseDto::fromEntity).toList();
        return new NewsFeedDetailResponseDto(newsFeed, comments);
    }

    // 뉴스피드 수정
    public NewsFeedResponseDto updateNewsFeed(Long newsFeedId, Long memberId, NewsFeedUpdateRequestDto requestDto) {
        NewsFeed newsFeed = getExistingNewsFeed(newsFeedId);
        validateNewsFeedOwner(newsFeed, memberId);
        newsFeed.updateNewsFeed(requestDto.getTitle(), requestDto.getContents());
        return NewsFeedResponseDto.fromEntity(newsFeed);
    }

    // 뉴스피드 삭제 (댓글도 삭제)
    public void deleteNewsFeed(Long newsFeedId, Long memberId) {
        NewsFeed newsFeed = getExistingNewsFeed(newsFeedId);
        validateNewsFeedOwner(newsFeed, memberId);

        // 뉴스피드에 달린 댓글 먼저 삭제
        commentRepository.deleteByNewsFeedId(newsFeedId);

        // 뉴스피드 삭제
        newsFeedRepository.delete(newsFeed);
    }

    // 친구들의 뉴스피드 조회 (최신순, 페이징)
    public PageModelDto<NewsFeedResponseDto> getFriendNewsFeeds(Long memberId, int page, int size) {
        List<Long> friendIds = friendsRepository.findAcceptedFriendsIdByMemberId(memberId);
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findByMemberIdInOrderByCreatedAtDesc(friendIds, PageRequest.of(page - 1, size));
        return createPageResponse(newsFeedPage);
    }

    // 특정 친구의 뉴스피드 조회 (최신순, 페이징)
    public PageModelDto<NewsFeedResponseDto> getSpecificFriendNewsFeeds(Long memberId, Long friendId, int page, int size) {
        validateFriendship(memberId, friendId);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findByMemberIdOrderByCreatedAtDesc(friendId, pageable);
        return createPageResponse(newsFeedPage);
    }

    // 특정 회원의 뉴스피드 조회 (최신순, 페이징)
    @Transactional(readOnly = true)
    public PageModelDto<NewsFeedResponseDto> getMemberNewsFeeds(Long memberId, Pageable pageable) {
        Page<NewsFeed> page = newsFeedRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
        return createPageResponse(page);
    }

    //친구 여부 검증
    private void validateFriendship(Long memberId, Long friendId) {
        List<Long> friendIds = friendsRepository.findAcceptedFriendsIdByMemberId(memberId);
        if (!friendIds.contains(friendId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "NO_PERMISSION", "이 사용자는 친구가 아닙니다.");
        }
    }

    //뉴스피드 작성자 검증 (수정/삭제시)
    private void validateNewsFeedOwner(NewsFeed newsFeed, Long memberId) {
        if (!newsFeed.getMember().getId().equals(memberId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "NO_PERMISSION", "본인이 작성한 뉴스피드만 수정 및 삭제할 수 있습니다.");
        }
    }

    //특정 뉴스피드 조회 (예외 처리 포함)
    private NewsFeed getExistingNewsFeed(Long newsFeedId) {
        return newsFeedRepository.findById(newsFeedId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", NEWSFEED_NOT_FOUND_MSG));
    }

    //페이지네이션 응답 생성 (중복 제거)
    private PageModelDto<NewsFeedResponseDto> createPageResponse(Page<NewsFeed> newsFeedPage) {
        List<NewsFeedResponseDto> content = newsFeedPage.getContent().stream()
                .map(NewsFeedResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new PageModelDto<>(content, newsFeedPage.getNumber() + 1, newsFeedPage.getTotalPages(), newsFeedPage.getTotalElements());
    }
}
