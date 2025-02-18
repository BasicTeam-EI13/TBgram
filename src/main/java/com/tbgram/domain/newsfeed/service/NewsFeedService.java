package com.tbgram.domain.newsfeed.service;

import com.tbgram.domain.auth.exception.CustomException;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.newsfeed.dto.request.NewsFeedRequestDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.dto.response.NewsPageResponseDto;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsFeedService {
    private final NewsFeedRepository newsFeedRepository;
    private final MemberRepository memberRepository;

    //뉴스피드 작성
    @Transactional
    public NewsFeedResponseDto createNewsFeed(Long memberId, NewsFeedRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."));

        NewsFeed newsFeed = NewsFeed.builder()
                .member(member)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .build();

        newsFeed = newsFeedRepository.save(newsFeed);
        return new NewsFeedResponseDto(newsFeed);
    }

    //뉴스피드 전체 조회 (최신순, 페이징)
    @Transactional(readOnly = true)
    public NewsPageResponseDto<NewsFeedResponseDto> getAllNewsFeeds(Pageable pageable) {
        Page<NewsFeed> page = newsFeedRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc(pageable);

        List<NewsFeedResponseDto> content = page.getContent()
                .stream()
                .map(NewsFeedResponseDto::new)
                .toList();

        return new NewsPageResponseDto<>(content, page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.isFirst(), page.isLast());
    }

    //뉴스피드 상세 조회
    @Transactional(readOnly = true)
    public NewsFeedResponseDto getNewsFeed(Long id) {
        NewsFeed newsFeed = newsFeedRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 게시글입니다."));

        if (newsFeed.getDeletedAt() != null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "NEWSFEED_DEACTIVATED", "비활성화된 게시글입니다.");
        }

        return new NewsFeedResponseDto(newsFeed);
    }

    //뉴스피드 수정 (본인만 가능)
    @Transactional
    public NewsFeedResponseDto updateNewsFeed(Long id, Long memberId, NewsFeedRequestDto requestDto) {
        NewsFeed newsFeed = newsFeedRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 게시글입니다."));

        // 본인만 수정 가능하도록 체크
        if (!newsFeed.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "FORBIDDEN", "수정 권한이 없습니다.");
        }

        newsFeed.update(requestDto.getTitle(), requestDto.getContents());
        return new NewsFeedResponseDto(newsFeed);
    }

    //뉴스피드 삭제 (본인만 가능)
    @Transactional
    public void deleteNewsFeed(Long id, Long memberId) {
        NewsFeed newsFeed = newsFeedRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "NEWSFEED_NOT_FOUND", "존재하지 않는 게시글입니다."));

        if (!newsFeed.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "FORBIDDEN", "본인의 게시글만 삭제할 수 있습니다.");
        }

        newsFeed.deactivate();
        newsFeedRepository.save(newsFeed);
    }
}
