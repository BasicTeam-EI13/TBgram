package com.tbgram.domain.comment.repository;

import com.tbgram.domain.comment.entity.Comment;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNewsFeedIdOrderByCreatedAtDesc(Long newsFeedId);
    List<Comment> findByNewsFeedOrderByCreatedAtDesc(NewsFeed newsFeed);


    //List<Comment> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
    void deleteByNewsFeedId(Long newsFeedId);
}
