package team4.footwithme.chat.service;

import team4.footwithme.chat.service.request.ChatServiceRequest;

public interface ChatService {

    void sendMessage(ChatServiceRequest request, String email);
}
