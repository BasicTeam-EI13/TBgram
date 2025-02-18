package com.tbgram.domain.member.repository;

import com.tbgram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Object> findByEmail(String email);

    default Member findMemberByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.")
        );
    }

    Member findEmailByNickName(String nickName);

    Optional<Object> findByNickName(String nickName);
}
