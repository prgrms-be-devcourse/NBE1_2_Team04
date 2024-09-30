package team4.footwithme.chat.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import team4.footwithme.chat.service.request.ChatServiceRequest;
import team4.footwithme.chat.service.response.ChatResponse;

public interface ChatService {

    void sendMessage(ChatServiceRequest request, String email);

    Slice<ChatResponse> getChatList(Long chatroomId, PageRequest pageRequest, String email);
}
