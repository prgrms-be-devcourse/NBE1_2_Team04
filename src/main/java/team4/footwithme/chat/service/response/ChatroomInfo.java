package team4.footwithme.chat.service.response;

import team4.footwithme.chat.domain.Chatroom;

public record ChatroomInfo(
        Long chatroomId,
        String name
) {
    public ChatroomInfo(Chatroom chatroom) {
        this(
                chatroom.getChatroomId(),
                chatroom.getName()
        );
    }
}
