package team4.footwithme.chat.api

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import team4.footwithme.chat.api.request.ChatMemberRequest
import team4.footwithme.chat.service.ChatMemberService
import team4.footwithme.chat.service.response.ChatMemberResponse
import team4.footwithme.global.api.ApiResponse

@RestController
@RequestMapping("/api/v1/chat/member")
class ChatMemberApi(private val chatMemberService: ChatMemberService) {
    /**
     * 채팅방 초대
     */
    @PostMapping
    fun inviteChatMember(@RequestBody chatMemberRequest: @Valid ChatMemberRequest?): ApiResponse<ChatMemberResponse?> {
        return ApiResponse.Companion.created<ChatMemberResponse?>(chatMemberService.joinChatMember(chatMemberRequest!!.toServiceRequest()))
    }

    /**
     * 채팅방 나감
     */
    @DeleteMapping
    fun removeChatMember(@RequestBody chatMemberRequest: @Valid ChatMemberRequest?): ApiResponse<ChatMemberResponse?> {
        return ApiResponse.Companion.ok<ChatMemberResponse?>(chatMemberService.leaveChatMember(chatMemberRequest!!.toServiceRequest()))
    }
}
