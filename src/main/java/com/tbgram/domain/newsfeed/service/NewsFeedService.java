package com.tbgram.domain.newsfeed.service;

import com.tbgram.domain.comment.dto.response.CommentResponseDto;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.friends.respository.FriendsRepository;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import org.springframework.data.domain.Page;

import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedCreateRequestDto;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedUpdateRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedDetailResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.entity.NewsFeed;

import com.tbgram.domain.auth.exception.ApiException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(PageRequest.of(page - 1, size));
        return createPageResponse(newsFeedPage);

//         Page<NewsFeed> newsFeedPage = newsFeedRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(PageRequest.of(page -1, size));
//         List<NewsFeedResponseDto> content = newsFeedPage.map(NewsFeedResponseDto::fromEntity).toList();
//         return new PageModelDto<>(content, newsFeedPage.getNumber() + 1, newsFeedPage.getTotalPages(), newsFeedPage.getTotalElements());
    }

    // 뉴스피드 상세 조회 (댓글 포함)
    public NewsFeedDetailResponseDto getNewsFeedDetail(Long newsFeedId) {
        NewsFeed newsFeed = newsFeedRepository.findByIdAndDeletedAtIsNull(newsFeedId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 뉴스피드입니다."));
        List<CommentResponseDto> comments = commentRepository.findByNewsFeedIdOrderByCreatedAtDesc(newsFeedId)
                .stream().map(CommentResponseDto::fromEntity).toList();
        return new NewsFeedDetailResponseDto(newsFeed, comments);
    }

    // 뉴스피드 수정
    public NewsFeedResponseDto updateNewsFeed(Long newsFeedId, Long memberId, NewsFeedUpdateRequestDto requestDto) {
        NewsFeed newsFeed = newsFeedRepository.findById(newsFeedId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 뉴스피드입니다."));
        if (!memberId.equals(newsFeed.getMember().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "NO_PERMISSION", "본인이 작성한 뉴스피드만 수정할 수 있습니다.");
        }
        newsFeed.updateNewsFeed(requestDto.getTitle(), requestDto.getContents());
        return NewsFeedResponseDto.fromEntity(newsFeed);
    }

    // 뉴스피드 삭제
    public void deleteNewsFeed(Long newsFeedId, Long memberId) {
        NewsFeed newsFeed = newsFeedRepository.findById(newsFeedId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 뉴스피드입니다."));
        if (!newsFeed.getMember().getId().equals(memberId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "NO_PERMISSION", "본인이 작성한 뉴스피드만 삭제할 수 있습니다.");
        }
        newsFeed.delete();
    }

    // 친구들의 뉴스피드 조회 (최신순, 페이징)
    public PageModelDto<NewsFeedResponseDto> getFriendNewsFeeds(Long memberId, int page, int size) {
        List<Long> friendIds = friendsRepository.findAcceptedFriendsIdByMemberId(memberId);
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findByMemberIdInAndDeletedAtIsNullOrderByCreatedAtDesc(friendIds, PageRequest.of(page - 1, size));
        return createPageResponse(newsFeedPage);
    }

    // 특정 친구의 뉴스피드 조회
    public PageModelDto<NewsFeedResponseDto> getSpecificFriendNewsFeeds(Long memberId, Long friendId, int page, int size) {
        validateFriendship(memberId, friendId);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(friendId, pageable);
        return createPageResponse(newsFeedPage);
    }

    // 뉴스피드 deletedAt 처리
    @Transactional
    public void softDeleteByMemberId(Long memberId) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<NewsFeed> newsFeedPage = newsFeedRepository.findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(memberId, pageable);
        newsFeedPage.forEach(NewsFeed::delete);
    }

    // 친구 여부 검증
    private void validateFriendship(Long memberId, Long friendId) {
        List<Long> friendIds = friendsRepository.findAcceptedFriendsIdByMemberId(memberId);
        if (!friendIds.contains(friendId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 사용자는 친구가 아닙니다.");
        }
    }

    // 페이지네이션 응답 생성 (중복 제거)
    private PageModelDto<NewsFeedResponseDto> createPageResponse(Page<NewsFeed> newsFeedPage) {
         List<NewsFeedResponseDto> content = newsFeedPage.map(NewsFeedResponseDto::fromEntity).toList();
         return new PageModelDto<>(content, newsFeedPage.getNumber() + 1, newsFeedPage.getTotalPages(), newsFeedPage.getTotalElements());
    }


    // 멤버Id로 최신 뉴스피드 조회 및 페이징
    @Transactional(readOnly = true)
    public PageModelDto<NewsFeedResponseDto> getMemberNewsFeeds(Long memberId, Pageable pageable) {

        // 해당 멤버의 모든 뉴스피드를 최신순으로 페이지네이션 적용하여 조회
        Page<NewsFeed> page = newsFeedRepository.findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(memberId, pageable);

        // 뉴스피드 목록을 DTO로 변환
        List<NewsFeedResponseDto> newsFeedResponseDtos = page.getContent().stream()
                .map(NewsFeedResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageModelDto<>(newsFeedResponseDtos, page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }

    // 멤버ID로 뉴스피드 조회
    @Transactional(readOnly = true)
    public List<NewsFeed> getNewsFeedsByMemberId(Long memberId) {
        return newsFeedRepository.findByMemberIdAndDeletedAtIsNull(memberId);
    }
}
