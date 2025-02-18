package com.tbgram.domain.friends.respository;

import com.tbgram.domain.friends.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends,Long> {
    Optional<Friends> findBySenderIdAndReceiverId(Long senderId,Long receiverId);

    default Friends findByIdOrElseThrow(Long friendsId) {
        return findById(friendsId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다.")
        );
    }

    // 친구 ID 목록 조회 (ACCEPTED 상태만 포함)
    @Query("SELECT CASE " +
            "WHEN f.sender.id = :memberId THEN f.receiver.id " +
            "WHEN f.receiver.id = :memberId THEN f.sender.id " +
            "END " +
            "FROM Friends f " +
            "WHERE (f.sender.id = :memberId OR f.receiver.id = :memberId) " +
            "AND f.status = 'ACCEPTED'")
    List<Long> findAcceptedFriendIdsByMemberId(@Param("memberId") Long memberId);
}
