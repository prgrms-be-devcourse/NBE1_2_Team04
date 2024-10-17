package team4.footwithme.chat.api.request

import jakarta.validation.constraints.NotBlank
import team4.footwithme.chat.service.request.ChatroomServiceRequest

@JvmRecord
data class ChatroomRequest(
    val name: @NotBlank(message = "채팅방 이름은 필수입니다.") String?
) {
    fun toServiceRequest(): ChatroomServiceRequest {
        return ChatroomServiceRequest(name)
    }
}
