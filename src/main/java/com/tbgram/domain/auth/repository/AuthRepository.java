package com.tbgram.domain.auth.repository;


import com.tbgram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    default Member findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."));
    }
}
