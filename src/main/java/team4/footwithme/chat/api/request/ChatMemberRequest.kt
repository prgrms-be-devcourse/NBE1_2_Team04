package team4.footwithme.chat.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.chat.service.request.ChatMemberServiceRequest

@JvmRecord
data class ChatMemberRequest(
    val chatroomId: @NotNull(message = "채팅방 아이디는 필수입니다.") Long?,
    val memberId: @NotNull(message = "회원 아이디는 필수입니다.") Long?
) {
    fun toServiceRequest(): ChatMemberServiceRequest {
        return ChatMemberServiceRequest(chatroomId, memberId)
    }
}
