package com.tbgram.domain.friends.dto.request;

import com.tbgram.domain.friends.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendsRequestStatusDto {
    private RequestStatus status;
}
