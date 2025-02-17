package com.tbgram.domain.comment.entity;

import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // FK
    private User user;

    @ManyToOne
    @JoinColumn(name = "newsFeed_id")   //
    private NewsFeed newsFeed;
}
