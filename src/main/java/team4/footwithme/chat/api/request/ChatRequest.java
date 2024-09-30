package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotBlank;
import team4.footwithme.chat.service.request.ChatServiceRequest;

public record ChatRequest(
        @NotBlank(message = "채팅방 아이디는 필수입니다.")
        Long chatroomId,
        @NotBlank(message = "채팅 메세지는 필수입니다.")
        String message
) {
    public ChatServiceRequest toServiceRequest() {
        return new ChatServiceRequest(chatroomId, message);
    }
}
