package team4.footwithme.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.service.response.ChatResponse;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            String channel = new String(pattern, StandardCharsets.UTF_8);
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
