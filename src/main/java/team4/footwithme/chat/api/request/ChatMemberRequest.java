package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;

public record ChatMemberRequest(
        @NotNull(message = "채팅방 아이디는 필수입니다.")
        Long chatroomId,
        @NotNull(message = "회원 아이디는 필수입니다.")
        Long memberId) {
    public ChatMemberServiceRequest toServiceRequest() {
        return new ChatMemberServiceRequest(chatroomId, memberId);
    }
}
