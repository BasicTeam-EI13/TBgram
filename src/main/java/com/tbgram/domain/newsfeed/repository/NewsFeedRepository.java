package com.tbgram.domain.newsfeed.repository;

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

    //특정 회원이 작성한 뉴스피드 전체 조회
    List<NewsFeed> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    //특정 회원이 작성한 뉴스피드 삭제
    void deleteByMemberId(Long memberId);
}
