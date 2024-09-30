package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatMemberRequest;
import team4.footwithme.chat.service.ChatMemberService;
import team4.footwithme.global.api.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ChatMemberApi {
    private final ChatMemberService chatMemberService;

    /**
     * 채팅방 초대
     */
    @PostMapping("/chat/member")
    public ApiResponse<String> inviteChatMember(@RequestBody @Valid ChatMemberRequest chatMemberRequest) {
        String message = chatMemberService.joinChatMember(chatMemberRequest.toServiceRequest());
        return ApiResponse.created(message);
    }

    /**
     * 채팅방 나감
     */
    @DeleteMapping("/chat/member")
    public ApiResponse<String> removeChatMember(@RequestBody @Valid ChatMemberRequest chatMemberRequest) {
        String message = chatMemberService.leaveChatMember(chatMemberRequest.toServiceRequest());
        return ApiResponse.ok(message);
    }

}
