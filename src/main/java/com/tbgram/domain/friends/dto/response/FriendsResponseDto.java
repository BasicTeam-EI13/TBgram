package com.tbgram.domain.friends.dto.response;

import com.tbgram.domain.friends.dto.request.FriendsRequestDto;
import com.tbgram.domain.friends.entity.Friends;
import com.tbgram.domain.friends.enums.RequestStatus;
import com.tbgram.domain.member.dto.MemberResponseDto;
import com.tbgram.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendsResponseDto {
    private Long id;
    private Long senderUserId;
    private Long receiverUserId;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FriendsResponseDto fromEntity(Friends friends) {
        return FriendsResponseDto.builder()
                .id(friends.getId())
                .senderUserId(friends.getSender().getId())
                .receiverUserId(friends.getReceiver().getId())
                .status(friends.getStatus())
                .createdAt(friends.getCreatedAt())
                .updatedAt(friends.getUpdatedAt())
                .build();
    }
}
