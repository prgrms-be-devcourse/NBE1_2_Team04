package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatroomRequest;
import team4.footwithme.chat.service.ChatroomService;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/room")
public class ChatroomApi {
    private final ChatroomService chatroomService;

    /**
     * 채팅방 생성
     * 팀, 예약 생성시 같이 실행해주기
     */
    @PostMapping
    public ApiResponse<ChatroomResponse> createChatroom(@RequestBody @Valid ChatroomRequest chatroomRequest) {
        return ApiResponse.created(chatroomService.createChatroom(chatroomRequest.toServiceRequest()));
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/{chatroomId}")
    public ApiResponse<Long> deleteChatroom(@PathVariable("chatroomId") Long chatroomId) {
        return ApiResponse.ok(chatroomService.deleteChatroomByChatroomId(chatroomId));
    }

    /**
     * 채팅방 수정
     */
    @PutMapping("/{chatroomId}")
    public ApiResponse<ChatroomResponse> updateChatroom(@PathVariable("chatroomId") Long chatroomId, @RequestBody @Valid ChatroomRequest chatroomRequest) {
        return ApiResponse.ok(chatroomService.updateChatroom(chatroomId, chatroomRequest.toServiceRequest()));
    }

    /**
     * 채팅방 조회
     */
    @GetMapping
    public ApiResponse<List<ChatroomResponse>> getMyChatroomList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(chatroomService.getMyChatroom(principalDetails.getMember()));
    }
}
