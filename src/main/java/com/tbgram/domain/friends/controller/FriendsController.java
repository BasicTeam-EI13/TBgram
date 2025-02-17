package com.tbgram.domain.friends.controller;

import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.friends.dto.request.FriendsRequestDto;
import com.tbgram.domain.friends.dto.request.FriendsRequestStatusDto;
import com.tbgram.domain.friends.dto.response.FriendsResponseDto;
import com.tbgram.domain.friends.service.FriendsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendsController {
    private final FriendsService friendsService;

    @PostMapping
    private ResponseEntity<FriendsResponseDto> friendsRequest(@RequestBody FriendsRequestDto requestDto,
                                                              HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long senderId = ((SessionUser) session.getAttribute("loginUser")).getId();
        return ResponseEntity.ok(friendsService.friendsRequest(senderId, requestDto.getReceiverId()));
    }

    @PatchMapping("/{friendsId}")
    private ResponseEntity<FriendsResponseDto> friendsResponse(@PathVariable Long friendsId,
                                                                @RequestBody FriendsRequestStatusDto statusDto,
                                                                HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long receiverId = ((SessionUser) session.getAttribute("loginUser")).getId();
        return ResponseEntity.ok(friendsService.friensResponse(friendsId,receiverId, statusDto.getStatus()));
    }

}
