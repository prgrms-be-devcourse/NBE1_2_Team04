package team4.footwithme.chat.service.response;

import team4.footwithme.chat.domain.Chatroom;

public record ChatroomResponse(
        Long chatroomId,
        String name) {

    public ChatroomResponse(Chatroom chatroom) {
        this(
                chatroom.getChatroomId(),
                chatroom.getName()
        );
    }
}
