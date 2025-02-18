package com.tbgram.domain.friends.controller;

import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.common.annotation.LoginUser;
import com.tbgram.domain.friends.dto.request.FriendsRequestDto;
import com.tbgram.domain.friends.dto.request.FriendsRequestStatusDto;
import com.tbgram.domain.friends.dto.response.FriendsResponseDto;
import com.tbgram.domain.friends.dto.response.PageModelDto;
import com.tbgram.domain.friends.entity.Friends;
import com.tbgram.domain.friends.respository.FriendsRepository;
import com.tbgram.domain.friends.service.FriendsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendsController {
    private final FriendsService friendsService;
    private final FriendsRepository friendsRepository;

    /**
     * 친구요청
     *
     * @param requestDto 요청을 받는 member
     * @param senderId 로그인된 사용자의 ID값
     * @return 생성된 친구 Entity 정보 반환
     */

    @PostMapping
    private ResponseEntity<FriendsResponseDto> friendsRequest(@RequestBody FriendsRequestDto requestDto,
                                                              @LoginUser Long senderId) {
        return ResponseEntity.ok(friendsService.friendsRequest(senderId, requestDto.getReceiverId()));
    }

    /**
     *
     * 친구 요청에 대한 응답
     * @param friendsId sender와 receiver의 관계 정보를 담은 객체의 ID값
     * @param statusDto 요청에 대한 응답(PENDING, ACCEPTED, REJECTED)
     * @param receiverId 로그인된 사용자의 ID값
     * @return 업데이트된 친구 Entity 정보 반환
     */
    @PutMapping("/{friendsId}")
    private ResponseEntity<?> friendsResponse(@PathVariable Long friendsId,
                                              @RequestBody FriendsRequestStatusDto statusDto,
                                              @LoginUser Long receiverId) {
        return friendsService.friensResponse(friendsId, receiverId, statusDto.getStatus())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    /**
     * 친구 삭제
     * @param friendsId 삭제할 친구 관계의 id
     * @param userId 로그인된 사용자의 ID값
     * @return HTTP 상태코드 200(OK)
     */
    @DeleteMapping("/{friendsId}")
    private ResponseEntity<Void> deleteFriends(@PathVariable Long friendsId,
                                               @LoginUser Long userId) {
        friendsService.deleteFriends(friendsId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageModelDto<FriendsResponseDto>> friendsList(Pageable pageable,
                                                                        HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long userId = ((SessionUser) session.getAttribute("loginUser")).getId();
        return ResponseEntity.ok(friendsService.getFriendsList(userId,pageable));
    }
}
