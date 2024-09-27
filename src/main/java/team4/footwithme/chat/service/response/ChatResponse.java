package team4.footwithme.chat.service.response;

import team4.footwithme.chat.domain.Chat;

import java.time.LocalDateTime;

public record ChatResponse(
        Long chatId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ChatroomInfo chatRoomInfo,
        ChatMemberInfo memberInfo,
        String text) {

    public ChatResponse(Chat chat){
        this(
                chat.getChatId(),
                chat.getCreatedAt(),
                chat.getUpdatedAt(),
                new ChatroomInfo(chat.getChatRoom()),
                new ChatMemberInfo(chat.getMember()),
                chat.getText()
        );
    }

}
