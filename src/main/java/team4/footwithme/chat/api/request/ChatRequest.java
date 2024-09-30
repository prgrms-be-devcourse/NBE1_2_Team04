package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.chat.service.request.ChatServiceRequest;

public record ChatRequest(
        @NotNull(message = "채팅방 아이디는 필수입니다.")
        Long chatroomId,
        @NotNull(message = "채팅 메세지는 필수입니다.")
        String message
) {
    public ChatServiceRequest toServiceRequest() {
        return new ChatServiceRequest(chatroomId, message);
    }
}
