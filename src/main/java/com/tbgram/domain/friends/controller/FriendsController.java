package com.tbgram.domain.friends.controller;

import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.friends.dto.request.FriendsRequestDto;
import com.tbgram.domain.friends.dto.request.FriendsRequestStatusDto;
import com.tbgram.domain.friends.dto.response.FriendsResponseDto;
import com.tbgram.domain.friends.dto.response.PageModelDto;
import com.tbgram.domain.friends.enums.RequestStatus;
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
     * 친구 요청
     *
     * @param requestDto 요청을 받는 member
     * @param request    로그인 정보가 담긴 session 객체
     * @return 생성된 친구 Entity 정보 반환
     */

    @PostMapping
    private ResponseEntity<FriendsResponseDto> friendsRequest(@RequestBody FriendsRequestDto requestDto,
                                                              HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long senderId = ((SessionUser) session.getAttribute("loginUser")).getId();
        return ResponseEntity.ok(friendsService.friendsRequest(senderId, requestDto.getReceiverId()));
    }

    /**
     * 친구 요청에 대한 응답
     *
     * @param friendsId sender와 receiver의 관계 정보를 담은 객체의 id
     * @param statusDto 요청에 대한 응답(PENDING, ACCEPTED, REJECTED)
     * @param request   로그인 정보가 담긴 session 객체
     * @return 업데이트된 친구 Entity 정보 반환
     */
    @PutMapping("/{friendsId}")
    private ResponseEntity<?> friendsResponse(@PathVariable Long friendsId,
                                              @RequestBody FriendsRequestStatusDto statusDto,
                                              HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long receiverId = ((SessionUser) session.getAttribute("loginUser")).getId();

        return friendsService.friensResponse(friendsId, receiverId, statusDto.getStatus())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    /**
     * 친구 삭제
     *
     * @param friendsId 삭제할 친구 관계의 id
     * @param request   로그인 정보가 담긴 session 객체
     * @return HTTP 상태코드 200(OK)
     */
    @DeleteMapping("/{friendsId}")
    private ResponseEntity<Void> deleteFriends(@PathVariable Long friendsId,
                                               HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long userId = ((SessionUser) session.getAttribute("loginUser")).getId();
        friendsService.deleteFriends(friendsId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * RequestParam의 status에 따라 친구목록, 친구요청 목록을 반환합니다.
     * @param pageable 페이징 정보가 담긴 객체
     * @param status (ACCEPTED or PENDING)
     * @param request 로그인 정보가 담긴 session 객체
     * @return 페이징된 친구목록, 친구요청목록을 반환합니다.
     */
    @GetMapping
    public ResponseEntity<PageModelDto<FriendsResponseDto>> friendsList(Pageable pageable,
                                                                        @RequestParam RequestStatus status,
                                                                        HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Long userId = ((SessionUser) session.getAttribute("loginUser")).getId();
        log.info("status : {}",status);
        return ResponseEntity.ok(friendsService.getFriendsList(userId,status,pageable));
    }
}
