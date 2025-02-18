package com.tbgram.domain.friends.service;

import com.tbgram.domain.friends.dto.response.FriendsResponseDto;
import com.tbgram.domain.friends.entity.Friends;
import com.tbgram.domain.friends.enums.RequestStatus;
import com.tbgram.domain.friends.respository.FriendsRepository;
import com.tbgram.domain.member.entity.Member;
import com.tbgram.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendRepository;
    private final MemberRepository memberRepository;

    public FriendsResponseDto friendsRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "보내는 회원을 찾을 수 없습니다."));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "요청 대상을 찾을 수 없습니다."));

        Optional<Friends> request = friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .or(() -> friendRepository.findBySenderIdAndReceiverId(receiverId, senderId));

        if (request.isPresent()) {
            if (request.get().getStatus() == RequestStatus.PENDING) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대기중인 요청입니다.");
            } else if (request.get().getStatus() == RequestStatus.ACCEPTED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 친구로 등록된 사용자입니다.");
            }
        }

        Friends friendsRequest = new Friends(sender, receiver, RequestStatus.PENDING);
        Friends addFriends = friendRepository.save(friendsRequest);
        return FriendsResponseDto.fromEntity(addFriends);
    }

    @Transactional
    public Optional<FriendsResponseDto> friensResponse(Long friendsId, Long receiverId, RequestStatus status) {
        Friends friendsRequest = friendRepository.findByIdOrElseThrow(friendsId);
        if (friendsRequest.getReceiver().getId() != receiverId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "응답자ID가 일치하지 않습니다.");
        }
        if(friendsRequest.getStatus() != RequestStatus.PENDING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대기중인 요청이 아닙니다.");
        }

        friendsRequest.updateStatus(status);
        friendRepository.flush();

        if(friendsRequest.getStatus() == RequestStatus.REJECTED){
            friendRepository.delete(friendsRequest);
            return Optional.empty();
        }

        return Optional.of(FriendsResponseDto.fromEntity(friendsRequest));
    }

    public void deleteFriends(Long friendsId, Long userId) {
        Friends friends = friendRepository.findByIdOrElseThrow(friendsId);
        Long senderId = friends.getSender().getId();
        Long receiverId = friends.getReceiver().getId();

        if (userId != senderId && userId != receiverId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
        if (friends.getStatus() == RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "요청이 진행중입니다.");
        }

        friendRepository.delete(friends);
    }
}
