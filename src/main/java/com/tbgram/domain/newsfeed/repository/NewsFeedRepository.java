package com.tbgram.domain.newsfeed.repository;

import com.tbgram.domain.newsfeed.entity.NewsFeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {
    // 최신순 정렬 (createdAt 기준 내림차순)
    Page<NewsFeed> findAllByOrderByCreatedAtDesc(Pageable pageable);
}