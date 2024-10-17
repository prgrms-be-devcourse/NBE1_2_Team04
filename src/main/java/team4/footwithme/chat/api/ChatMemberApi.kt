package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatMemberRequest;
import team4.footwithme.chat.service.ChatMemberService;
import team4.footwithme.chat.service.response.ChatMemberResponse;
import team4.footwithme.global.api.ApiResponse;

@RestController
@RequestMapping("/api/v1/chat/member")
public class ChatMemberApi {
    private final ChatMemberService chatMemberService;

    public ChatMemberApi(ChatMemberService chatMemberService) {
        this.chatMemberService = chatMemberService;
    }

    /**
     * 채팅방 초대
     */
    @PostMapping
    public ApiResponse<ChatMemberResponse> inviteChatMember(@RequestBody @Valid ChatMemberRequest chatMemberRequest) {
        return ApiResponse.created(chatMemberService.joinChatMember(chatMemberRequest.toServiceRequest()));
    }

    /**
     * 채팅방 나감
     */
    @DeleteMapping
    public ApiResponse<ChatMemberResponse> removeChatMember(@RequestBody @Valid ChatMemberRequest chatMemberRequest) {
        return ApiResponse.ok(chatMemberService.leaveChatMember(chatMemberRequest.toServiceRequest()));
    }

}
