package team4.footwithme.chat.service.response;

import team4.footwithme.chat.domain.ChatMember;

public record ChatMemberResponse(
    Long chatroomId,
    Long memberId
) {
    public ChatMemberResponse(ChatMember chatMember) {
        this(
            chatMember.getChatRoom().getChatroomId(),
            chatMember.getMember().getMemberId()
        );
    }
}
