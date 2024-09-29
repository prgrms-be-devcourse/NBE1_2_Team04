package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;

public record ChatroomRequest(
        @NotNull(message = "채팅방 이름은 필수입니다.")
        String name
) {
    public ChatroomServiceRequest toServiceRequest() {
        return new ChatroomServiceRequest(name);
    }
}
