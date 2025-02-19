package com.tbgram.domain.comment.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", nullable = false) // FK
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "newsFeed_id")   //
    private NewsFeed newsFeed;

    @Builder public Comment(String contents, Member member, NewsFeed newsFeed) {
        this.contents = contents;
        this.member = member;
        this.newsFeed = newsFeed;
    }

    public void update(String contents) {
        this.contents = contents;
    }
}
