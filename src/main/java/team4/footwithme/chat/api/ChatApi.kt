package team4.footwithme.chat.api

import jakarta.validation.Valid
import org.springframework.data.domain.*
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team4.footwithme.chat.api.request.ChatRequest
import team4.footwithme.chat.api.request.ChatUpdateRequest
import team4.footwithme.chat.service.ChatService
import team4.footwithme.chat.service.response.ChatResponse
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.member.jwt.PrincipalDetails

@RestController
@RequestMapping("/api/v1/chat/message")
class ChatApi(private val chatService: ChatService) {
    /**
     * 채팅 보내기
     */
    // TODO : test를 위해 email을 변수로 설정함. 나중에 매개변수에 @AuthenticationPrincipal에서 이메일 빼오기
    @MessageMapping("/api/v1/chat/message")
    fun sendMessage(request: @Valid ChatRequest?, @Header("Authorization") token: String) {
        chatService.sendMessage(request!!.toServiceRequest(), token)
    }

    /**
     * 채팅 조회
     *
     * @param chatroomId 채팅방 ID
     * @param page       현재 페이지
     * @param size       한페이지에 나타날 갯수
     * @return
     */
    @GetMapping("/{chatroomId}")
    fun getChatting(
        @PathVariable chatroomId: Long?,
        @RequestParam page: Int,
        @RequestParam size: Int,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Slice<ChatResponse>?> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by("createdAt").descending())
        return ApiResponse.Companion.ok<Slice<ChatResponse>?>(
            chatService.getChatList(
                chatroomId,
                pageRequest,
                principalDetails.member
            )
        )
    }

    /**
     * 채팅 수정
     */
    @PutMapping("/{chatId}")
    fun updateChatting(
        @PathVariable chatId: Long?,
        @RequestBody request: @Valid ChatUpdateRequest?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<ChatResponse?> {
        return ApiResponse.Companion.ok<ChatResponse?>(
            chatService.updateChat(
                request!!.toServiceRequest(),
                principalDetails.member,
                chatId
            )
        )
    }

    /**
     * 채팅 삭제
     */
    @DeleteMapping("/{chatId}")
    fun deleteChatting(
        @PathVariable chatId: Long?,
        @AuthenticationPrincipal principalDetails: PrincipalDetails
    ): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(chatService.deleteChat(principalDetails.member, chatId))
    }
}