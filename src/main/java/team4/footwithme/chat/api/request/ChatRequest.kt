package team4.footwithme.chat.api.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import team4.footwithme.chat.service.request.ChatServiceRequest

@JvmRecord
data class ChatRequest(
    val chatroomId: @NotNull(message = "채팅방 아이디는 필수입니다.") Long?,
    val message: @NotBlank(message = "채팅 메세지는 필수입니다.") String?
) {
    fun toServiceRequest(): ChatServiceRequest {
        return ChatServiceRequest(chatroomId, message)
    }
}
