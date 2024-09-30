package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatroomRequest;
import team4.footwithme.chat.service.ChatroomService;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.global.api.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ChatroomApi {
    private final ChatroomService chatroomService;

    /**
     * 채팅방 생성
     * 팀, 예약 생성시 같이 실행해주기
     */
    @PostMapping("/chat/room")
    public ApiResponse<ChatroomResponse> createChatroom(@RequestBody @Valid ChatroomRequest chatroomRequest) {
        ChatroomResponse response = chatroomService.createChatroom(chatroomRequest.toServiceRequest());
        return ApiResponse.created(response);
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/chat/room/{chatroomId}")
    public ApiResponse<Long> deleteChatroom(@PathVariable("chatroomId") @Valid Long chatroomId) {
        chatroomService.deleteChatroom(chatroomId);
        return ApiResponse.ok(chatroomId);
    }


}
