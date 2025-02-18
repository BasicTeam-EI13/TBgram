package com.tbgram.domain.newsfeed.repository;

import com.tbgram.domain.newsfeed.entity.NewsFeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    Page<NewsFeed> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);
    Page<NewsFeed> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    List<NewsFeed> findByMemberIdAndDeletedAtIsNull(Long memberId);

}