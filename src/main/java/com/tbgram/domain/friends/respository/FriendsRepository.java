package com.tbgram.domain.friends.respository;

import com.tbgram.domain.friends.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends,Long> {
    Optional<Friends> findBySenderIdAndReceiverId(Long senderId,Long receiverId);

    default Friends findByIdOrElseThrow(Long friendsId){
        return findById(friendsId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 요청입니다."));
    }
}
