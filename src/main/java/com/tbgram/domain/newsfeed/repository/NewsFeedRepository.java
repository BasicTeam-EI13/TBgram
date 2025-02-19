package com.tbgram.domain.newsfeed.repository;

import com.tbgram.domain.common.dto.response.PageModelDto;
import com.tbgram.domain.newsfeed.dto.response.NewsFeedResponseDto;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    //뉴스피드 전체 조회
    Page<NewsFeed> findAllByOrderByCreatedAtDesc(Pageable pageable);

    //특정 회원이 작성한 뉴스피드 조회
    Page<NewsFeed> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    //특정 뉴스피드 ID로 조회
    Optional<NewsFeed> findById(Long newsFeedId);

    //친구들의 뉴스피드 목록 조회
    Page<NewsFeed> findByMemberIdInOrderByCreatedAtDesc(List<Long> friendIds, Pageable pageable);


    //특정 회원이 작성한 뉴스피드 전체 조회 (삭제되지 않은 데이터만)
    List<NewsFeed> findByMemberIdAndDeletedAtIsNull(Long memberId);

    // 제목으로 뉴스피드 검색
    Page<NewsFeed> findByTitleContaining(String keyword, Pageable pageable);

    // 내용으로 뉴스피드 검색
    Page<NewsFeed> findByContentsContaining(String keyword, Pageable pageable);

    // 제목+내용으로 뉴스피드 검색 (제목, 내용중 키워드가 포함된 글 모두 검색)
    Page<NewsFeed> findByTitleContainingOrContentsContaining(String titleKeyword, String contentsKeyword, Pageable pageable);
}

    //특정 회원이 작성한 뉴스피드 전체 조회
    List<NewsFeed> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    //특정 회원이 작성한 뉴스피드 삭제
    void deleteByMemberId(Long memberId);
}
