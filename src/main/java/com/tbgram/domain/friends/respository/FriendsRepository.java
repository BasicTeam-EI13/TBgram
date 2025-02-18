package com.tbgram.domain.friends.respository;

import com.tbgram.domain.friends.entity.Friends;
import com.tbgram.domain.friends.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends,Long> {
    Optional<Friends> findBySenderIdAndReceiverId(Long senderId,Long receiverId);

    default Friends findByIdOrElseThrow(Long friendsId){
        return findById(friendsId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 요청입니다."));
    }

    @Query("SELECT f FROM Friends f WHERE (f.sender.id = :userId OR f.receiver.id = :userId) AND f.status = :status")
    Page<Friends> findFriendsByUserIdAndStatus(@Param("userId") Long userId,
                                               @Param("status") RequestStatus status,
                                               Pageable pageable);
}
