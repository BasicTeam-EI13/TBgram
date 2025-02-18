package com.tbgram.domain.comment.entity;

import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // FK
    private Member member;

    @ManyToOne
    @JoinColumn(name = "newsFeed_id")   //
    private NewsFeed newsFeed;
}
