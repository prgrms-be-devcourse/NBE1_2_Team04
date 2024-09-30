package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotBlank;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;

public record ChatMemberRequest(
        @NotBlank(message = "채팅방 아이디는 필수입니다.")
        Long chatroomId,
        @NotBlank(message = "회원 아이디는 필수입니다.")
        Long memberId) {
    public ChatMemberServiceRequest toServiceRequest() {
        return new ChatMemberServiceRequest(chatroomId, memberId);
    }
}
