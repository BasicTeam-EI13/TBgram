package com.tbgram.domain.comment.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.action.internal.OrphanRemovalAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // FK
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_feed_id")   //
    private NewsFeed newsFeed;

    public void update(String newContents) {
        //댓글 변경 검증
        if(newContents != null && !newContents.isEmpty()) {
            this.contents = newContents;
        }
    }
}

