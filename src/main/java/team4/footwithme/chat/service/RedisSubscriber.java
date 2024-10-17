package team4.footwithme.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.service.response.ChatResponse;

@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public RedisSubscriber(ObjectMapper objectMapper, SimpMessageSendingOperations messagingTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            Chat chat = objectMapper.readValue(publishMessage, Chat.class);

            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/api/v1/chat/room/" + chat.getChatRoom().getChatroomId(), new ChatResponse(chat));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 수신 오류입니다.");
        }
    }
}
