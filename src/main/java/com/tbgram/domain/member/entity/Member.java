package com.tbgram.domain.member.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor
@DynamicInsert
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column
    private String introduction;

    private LocalDateTime deletedAt;

    @Builder
    public Member(String email, String password, String nickName, String introduction) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        // null일 시에 기본값 설정 하고 싶었던 건데 이 member 생성자 자체가 적용이 안되네요
        // 일단 null 허용상태라 기능 작동에 문제는 없습니다. 수정해야 합니당.
        this.introduction = (introduction != null) ? introduction : "한줄소개 입력 부분입니다.";
    }

    public void updateMember(String nickName, String introduction){
        this.nickName = nickName;
        this.introduction = introduction;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }
}
