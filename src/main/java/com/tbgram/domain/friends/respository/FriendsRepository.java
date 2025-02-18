package com.tbgram.domain.friends.respository;

import com.tbgram.domain.friends.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends,Long> {
    Optional<Friends> findBySenderIdAndReceiverId(Long senderId,Long receiverId);
}
