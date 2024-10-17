package team4.footwithme.chat.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import team4.footwithme.chat.service.request.ChatServiceRequest;
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.member.domain.Member;

public interface ChatService {

    void sendMessage(ChatServiceRequest request, String token);

    Slice<ChatResponse> getChatList(Long chatroomId, PageRequest pageRequest, Member member);

    ChatResponse updateChat(ChatUpdateServiceRequest request, Member member, Long chatId);

    Long deleteChat(Member member, Long chatId);
}
