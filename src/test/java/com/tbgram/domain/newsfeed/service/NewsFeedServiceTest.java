package com.tbgram.domain.newsfeed.service;

import com.tbgram.domain.comment.entity.Comment;
import com.tbgram.domain.comment.repository.CommentRepository;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import com.tbgram.domain.newsfeed.entity.NewsFeed;
import com.tbgram.domain.newsfeed.repository.NewsFeedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NewsFeedServiceTest {

    @Autowired
    private NewsFeedRepository newsFeedRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private NewsFeed newsFeed;
    private Comment comment;

    @BeforeEach
    public void setUp() throws Exception {
        // 테스트용 회원 데이터 주입
        member = new Member();
        setField(member, "name", "Test User");
        setField(member, "email", "testuser@example.com");
        member = memberRepository.save(member);

        // 뉴스피드 데이터 주입 (리플렉션으로 생성)
        newsFeed = createNewsFeedWithReflection();
        setField(newsFeed, "member", member);
        setField(newsFeed, "content", "Test news feed content");
        newsFeed = newsFeedRepository.save(newsFeed);

        // 댓글 데이터 주입
        comment = new Comment();
        setField(comment, "newsFeed", newsFeed);
        setField(comment, "member", member);
        setField(comment, "content", "Test comment content");
        commentRepository.save(comment);
    }


    @Test
    public void testProfileFunctionality() {
        // 프로필 기능 테스트 (예: 회원 정보 확인)
        Member foundMember = memberRepository.findById(member.getId()).orElse(null);
        assertNotNull(foundMember);
        assertEquals("Test User", foundMember.getNickName());
    }

    // 리플렉션을 사용하여 NewsFeed 객체 생성
    private NewsFeed createNewsFeedWithReflection() throws Exception {
        // NewsFeed 클래스의 protected 생성자 호출
        Constructor<NewsFeed> constructor = NewsFeed.class.getDeclaredConstructor();
        constructor.setAccessible(true); // private, protected 생성자 접근 가능하게 설정
        return constructor.newInstance();
    }

    // 리플렉션을 이용하여 setter 메소드 호출
    private void setField(Object obj, String fieldName, Object value) throws Exception {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = obj.getClass().getMethod(methodName, value.getClass());
        method.invoke(obj, value);
    }
}