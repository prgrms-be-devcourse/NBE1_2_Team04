package team4.footwithme.chat.service.response;

import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.domain.ChatType;

import java.time.LocalDateTime;

public record ChatResponse(
    Long chatId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    ChatroomResponse chatroomResponse,
    ChatMemberInfo memberInfo,
    ChatType chatType,
    String text) {

    public ChatResponse(Chat chat) {
        this(
            chat.getChatId(),
            chat.getCreatedAt(),
            chat.getUpdatedAt(),
            new ChatroomResponse(chat.getChatRoom()),
            new ChatMemberInfo(chat.getMember()),
            chat.getChatType(),
            chat.getText()
        );
    }

}
