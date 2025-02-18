package com.tbgram.domain.newsfeed.repository;

import com.tbgram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    // 뉴스피드 목록 조회 (페이징 포함)
    Page<NewsFeed> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    // 뉴스피드 친구 목록 조회 (최신순, 페이징 포함)
    Page<NewsFeed> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    // 친구들의 뉴스피드 목록 조회 (최신순, 페이징 포함)
    Page<NewsFeed> findByMemberIdAndDeletedAtIsNull(List<Long> friendIds, Pageable pageable);

}

