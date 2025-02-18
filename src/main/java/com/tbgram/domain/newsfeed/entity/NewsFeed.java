package com.tbgram.domain.newsfeed.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import com.tbgram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_feed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsFeed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    private LocalDateTime deletedAt;

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void deactivate() {
        this.deletedAt = LocalDateTime.now();
    }


}
