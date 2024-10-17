package team4.footwithme.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.service.response.ChatResponse;

public interface CustomChatRepository {
    Slice<ChatResponse> findChatByChatroom(Chatroom chatroom, Pageable pageable);
}
