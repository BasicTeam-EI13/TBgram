package com.tbgram.domain.member.entity;

import com.tbgram.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column
    private String introduction;

    private LocalDateTime deletedAt;

    @Builder
    public Member(String email, String password, String nickName, String introduction) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.introduction = (introduction != null && !introduction.isBlank()) ? introduction : "한줄소개를 입력하세요.";
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
