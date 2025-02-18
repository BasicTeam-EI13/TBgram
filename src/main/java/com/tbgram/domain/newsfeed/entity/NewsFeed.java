package com.tbgram.domain.newsfeed.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import com.tbgram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "news_feed")
public class NewsFeed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    public void updateNewsFeed(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    private LocalDateTime deletedAt; // ✅ 탈퇴된 회원의 뉴스피드를 숨기기 위해 추가

    public void delete() {
        this.deletedAt = LocalDateTime.now(); // ✅ 뉴스피드 삭제 처리
    }
}
