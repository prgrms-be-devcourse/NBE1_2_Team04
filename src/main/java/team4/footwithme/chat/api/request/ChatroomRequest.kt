package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotBlank;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;

public record ChatroomRequest(
    @NotBlank(message = "채팅방 이름은 필수입니다.")
    String name
) {
    public ChatroomServiceRequest toServiceRequest() {
        return new ChatroomServiceRequest(name);
    }
}
