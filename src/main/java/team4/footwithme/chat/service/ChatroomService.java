package team4.footwithme.chat.service;

import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;

public interface ChatroomService {
    ChatroomResponse createChatroom(ChatroomServiceRequest request);

    Long deleteChatroom(Long chatroomId);

    ChatroomResponse updateChatroom(Long chatroomId, ChatroomServiceRequest request);
}
